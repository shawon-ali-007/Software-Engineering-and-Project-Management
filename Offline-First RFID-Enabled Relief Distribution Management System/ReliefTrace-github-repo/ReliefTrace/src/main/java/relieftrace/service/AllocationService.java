package relieftrace.service;

import relieftrace.db.DatabaseManager;
import relieftrace.model.*;
import java.util.*;

/** Explainable rule-based proposals. Rules are ASSUMPTIONS for the project and are configurable in the design. */
public class AllocationService {
    private final DatabaseManager db;
    private final AuditService audit;

    public AllocationService(DatabaseManager db, AuditService audit) { this.db = db; this.audit = audit; }

    public List<Allocation> generateProposals(int householdId, int roundId, User actor) {
        if (!actor.canApproveDistribution()) throw new SecurityException("Role not allowed to generate proposals");
        synchronized (db) {
            Household h = db.getHouseholds().get(householdId);
            DistributionRound r = db.getRounds().get(roundId);
            if (h == null || !h.isActive()) throw new IllegalStateException("Household not found or inactive");
            if (r == null || !r.isOpen()) throw new IllegalStateException("No open distribution round");
            int m = h.getMemberCount();
            List<Allocation> created = new ArrayList<>();
            propose(created, h, r, "Rice", 2 * m, "R1: 2 kg x " + m + " members", actor);
            propose(created, h, r, "Drinking Water", 3 * m, "R2: 3 bottles x " + m + " members", actor);
            propose(created, h, r, "Blanket", (m + 1) / 2, "R3: 1 blanket per 2 members, rounded up", actor);
            if (h.getChildrenUnder5() > 0)
                propose(created, h, r, "Baby Food Pack", h.getChildrenUnder5(),
                        "R4: 1 pack x " + h.getChildrenUnder5() + " child(ren) under 5", actor);
            return created;
        }
    }

    public List<Allocation> findFor(int householdId, int roundId) {
        List<Allocation> out = new ArrayList<>();
        synchronized (db) {
            for (Allocation a : db.getAllocations().values())
                if (a.getHouseholdId() == householdId && a.getRoundId() == roundId) out.add(a);
        }
        return out;
    }

    private void propose(List<Allocation> out, Household h, DistributionRound r, String itemName,
                         int ruleQty, String why, User actor) {
        ReliefItem item = null;
        for (ReliefItem i : db.getItems().values()) if (i.getItemName().equals(itemName)) item = i;
        if (item == null) return;
        for (Allocation a : db.getAllocations().values())            // duplicate proposal guard
            if (a.getRoundId() == r.getRoundId() && a.getHouseholdId() == h.getHouseholdId()
                    && a.getItemId() == item.getItemId()) return;
        int qty = Math.min(ruleQty, item.getQuantityOnHand());
        if (qty <= 0) return;
        String explanation = why + " = " + ruleQty
                + (qty < ruleQty ? " (limited by stock to " + qty + ")" : "");
        Allocation a = new Allocation(db.nextId(), r.getRoundId(), h.getHouseholdId(), item.getItemId(), qty, explanation);
        db.getAllocations().put(a.getAllocationId(), a);
        out.add(a);
        audit.append("ALLOCATION_PROPOSED", "Allocation", a.getAllocationId(), itemName + " x" + qty, actor);
    }
}
