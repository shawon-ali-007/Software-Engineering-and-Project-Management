package relieftrace.service;

import relieftrace.db.DatabaseManager;
import relieftrace.model.*;
import java.util.*;

public class InventoryService {
    private final DatabaseManager db;
    private final AuditService audit;

    public InventoryService(DatabaseManager db, AuditService audit) { this.db = db; this.audit = audit; }

    public List<ReliefItem> listItems() { synchronized (db) { return new ArrayList<>(db.getItems().values()); } }

    public void receiveStock(int itemId, int qty, User actor) {
        if (!actor.canManageStock()) throw new SecurityException("Only a Coordinator can receive stock");
        synchronized (db) {
            ReliefItem item = db.getItems().get(itemId);
            if (item == null) throw new IllegalArgumentException("Unknown item");
            item.increase(qty);
            audit.append("STOCK_RECEIVED", "ReliefItem", itemId, "+" + qty + " " + item.getUnit(), actor);
        }
    }
}
