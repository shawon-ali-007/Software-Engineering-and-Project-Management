package relieftrace.service;

import relieftrace.db.DatabaseManager;
import relieftrace.model.*;

public class DistributionService {
    private final DatabaseManager db;
    private final AuditService audit;

    public DistributionService(DatabaseManager db, AuditService audit) { this.db = db; this.audit = audit; }

    /**
     * Human-approved handover. All checks run BEFORE any change is made, inside one synchronized block,
     * mimicking the single SQLite transaction of the real design (see document section 12).
     */
    public Distribution approveAndRecord(int allocationId, String inputMethod, String cardUid, User actor) {
        if (!actor.canApproveDistribution()) throw new SecurityException("Role not allowed to approve distribution");
        synchronized (db) {
            Allocation a = db.getAllocations().get(allocationId);
            if (a == null) throw new IllegalArgumentException("Unknown allocation");
            if (!a.isProposed()) {
                audit.append("DUPLICATE_BLOCKED", "Allocation", allocationId, "status=" + a.getStatus(), actor);
                throw new IllegalStateException("Already handled: allocation status is " + a.getStatus());
            }
            DistributionRound r = db.getRounds().get(a.getRoundId());
            if (r == null || !r.isOpen()) throw new IllegalStateException("Distribution round is not open");
            ReliefItem item = db.getItems().get(a.getItemId());
            if (item.getQuantityOnHand() < a.getQuantity()) throw new IllegalStateException("Insufficient stock");

            item.decrease(a.getQuantity());                                   // stock deduction
            Distribution d = new Distribution(db.nextId(), allocationId, actor.getUserId(), inputMethod,
                    "RFID".equals(inputMethod) ? cardUid : null, a.getQuantity(), null);
            db.getDistributions().put(d.getDistributionId(), d);
            a.setStatus(Allocation.Status.DISTRIBUTED, null);
            audit.append("DISTRIBUTION_RECORDED", "Distribution", d.getDistributionId(),
                    item.getItemName() + " x" + a.getQuantity() + " via " + inputMethod, actor);
            return d;
        }
    }

    public void rejectAllocation(int allocationId, String reason, User actor) {
        if (!actor.canApproveDistribution()) throw new SecurityException("Role not allowed to reject");
        if (reason == null || reason.isBlank()) throw new IllegalArgumentException("A reason is required");
        synchronized (db) {
            Allocation a = db.getAllocations().get(allocationId);
            if (a == null || !a.isProposed()) throw new IllegalStateException("Allocation cannot be rejected");
            a.setStatus(Allocation.Status.REJECTED, reason.trim());
            audit.append("ALLOCATION_REJECTED", "Allocation", allocationId, reason.trim(), actor);
        }
    }
}
