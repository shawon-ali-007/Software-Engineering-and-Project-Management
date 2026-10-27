package relieftrace.service;

import relieftrace.db.DatabaseManager;
import relieftrace.model.*;
import java.util.*;

public class HouseholdService {
    private final DatabaseManager db;
    private final AuditService audit;

    public HouseholdService(DatabaseManager db, AuditService audit) { this.db = db; this.audit = audit; }

    public Household register(String head, String area, int members, int childrenUnder5,
                              int elderly, String damage, User actor) {
        if (!actor.canRegisterHouseholds()) throw new SecurityException("Role not allowed to register households");
        if (head == null || head.isBlank()) throw new IllegalArgumentException("Head name is required");
        if (area == null || area.isBlank()) throw new IllegalArgumentException("Area is required");
        if (members < 1 || members > 30) throw new IllegalArgumentException("Members must be 1-30");
        if (childrenUnder5 < 0 || elderly < 0 || childrenUnder5 + elderly > members)
            throw new IllegalArgumentException("Children/elderly counts exceed household size");
        if (!List.of("MINOR", "MAJOR", "SEVERE").contains(damage))
            throw new IllegalArgumentException("Invalid damage level");
        synchronized (db) {
            int id = db.nextId();
            String code = String.format("HH-%04d", db.getHouseholds().size() + 1);
            Household h = new Household(id, code, head.trim(), area.trim(), members, childrenUnder5, elderly, damage);
            db.getHouseholds().put(id, h);
            audit.append("HOUSEHOLD_REGISTERED", "Household", id, code, actor);
            return h;
        }
    }

    public List<Household> search(String keyword) {
        String k = keyword == null ? "" : keyword.trim().toLowerCase();
        List<Household> out = new ArrayList<>();
        synchronized (db) {
            for (Household h : db.getHouseholds().values())
                if (k.isEmpty() || h.getHouseholdCode().toLowerCase().contains(k)
                        || h.getHeadName().toLowerCase().contains(k) || h.getAreaName().toLowerCase().contains(k))
                    out.add(h);
        }
        return out;
    }

    /** Returns null when the UID is unknown, revoked, or the household is inactive. */
    public Household findByCardUid(String uidHex) {
        synchronized (db) {
            RfidCard c = db.getCards().get(normalise(uidHex));
            if (c == null || !c.isActive()) return null;
            Household h = db.getHouseholds().get(c.getHouseholdId());
            return (h != null && h.isActive()) ? h : null;
        }
    }

    public RfidCard assignCard(int householdId, String uidHex, User actor) {
        if (!actor.canRegisterHouseholds()) throw new SecurityException("Role not allowed to assign cards");
        String uid = normalise(uidHex);
        if (!uid.matches("([0-9A-F]{8}|[0-9A-F]{14}|[0-9A-F]{20})")) throw new IllegalArgumentException("Invalid UID format");
        synchronized (db) {
            if (!db.getHouseholds().containsKey(householdId)) throw new IllegalArgumentException("Unknown household");
            if (db.getCards().containsKey(uid)) throw new IllegalStateException("UID already registered");
            for (RfidCard c : db.getCards().values())
                if (c.getHouseholdId() == householdId && c.isActive())
                    throw new IllegalStateException("Household already has an active card");
            RfidCard card = new RfidCard(db.nextId(), uid, householdId);
            db.getCards().put(uid, card);
            audit.append("CARD_ASSIGNED", "RfidCard", card.getCardId(), "uid=" + uid + " household=" + householdId, actor);
            return card;
        }
    }
    static String normalise(String uid) { return uid == null ? "" : uid.trim().toUpperCase(); }
}
