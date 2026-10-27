package relieftrace.db;

import relieftrace.model.*;
import java.time.LocalDate;
import java.util.*;

/**
 * PROTOTYPE STAND-IN for the SQLite-backed DatabaseManager in the design.
 * It keeps data in memory so the sample compiles with no external libraries.
 * In the real project this class would open a JDBC connection to relieftrace.db
 * and run database/schema.sql. Services synchronise on this object.
 */
public class DatabaseManager {
    private final Map<Integer, Household> households = new LinkedHashMap<>();
    private final Map<String, RfidCard> cards = new LinkedHashMap<>();
    private final Map<Integer, ReliefItem> items = new LinkedHashMap<>();
    private final Map<Integer, DistributionRound> rounds = new LinkedHashMap<>();
    private final Map<Integer, Allocation> allocations = new LinkedHashMap<>();
    private final Map<Integer, Distribution> distributions = new LinkedHashMap<>();
    private final List<AuditRecord> auditRecords = new ArrayList<>();
    private int nextId = 1;

    public synchronized int nextId() { return nextId++; }
    public Map<Integer, Household> getHouseholds() { return households; }
    public Map<String, RfidCard> getCards() { return cards; }
    public Map<Integer, ReliefItem> getItems() { return items; }
    public Map<Integer, DistributionRound> getRounds() { return rounds; }
    public Map<Integer, Allocation> getAllocations() { return allocations; }
    public Map<Integer, Distribution> getDistributions() { return distributions; }
    public List<AuditRecord> getAuditRecords() { return auditRecords; }

    /** Synthetic data only. */
    public void seedSyntheticData() {
        addItem("Rice", "kg", 500, 50);
        addItem("Drinking Water", "bottle", 600, 60);
        addItem("Blanket", "pcs", 120, 10);
        addItem("Baby Food Pack", "pack", 40, 5);
        int hh1 = addHousehold("HH-0001", "Synthetic Head A", "Zone-1", 5, 1, 1, "MAJOR");
        addHousehold("HH-0002", "Synthetic Head B", "Zone-1", 3, 0, 0, "MINOR");
        addHousehold("HH-0003", "Synthetic Head C", "Zone-2", 7, 2, 1, "SEVERE");
        cards.put("04A1B2C3", new RfidCard(nextId(), "04A1B2C3", hh1));
        int id = nextId();
        rounds.put(id, new DistributionRound(id, "Round-2026-11-A", LocalDate.of(2026, 11, 20),
                LocalDate.of(2026, 11, 27), "OPEN"));
    }
    private void addItem(String name, String unit, int qty, int low) {
        int id = nextId(); items.put(id, new ReliefItem(id, name, unit, qty, low));
    }
    private int addHousehold(String code, String head, String area, int m, int c, int e, String d) {
        int id = nextId(); households.put(id, new Household(id, code, head, area, m, c, e, d)); return id;
    }
}
