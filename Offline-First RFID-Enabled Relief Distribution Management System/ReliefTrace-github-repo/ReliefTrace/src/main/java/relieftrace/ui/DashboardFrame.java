package relieftrace.ui;

import relieftrace.db.DatabaseManager;
import relieftrace.hardware.RfidSerialService;
import relieftrace.model.User;
import relieftrace.service.*;
import javax.swing.*;
import java.awt.*;

/** Main window: dashboard summary plus one tab per major screen. */
public class DashboardFrame extends JFrame {
    private final DatabaseManager db;
    private final RfidSerialService rfid;
    private final User actor;
    private final HouseholdPanel householdPanel;
    private final StockPanel stockPanel;
    private final DistributionPanel distributionPanel;
    private final AuditPanel auditPanel;
    private final JLabel summary = new JLabel();

    public DashboardFrame(DatabaseManager db, HouseholdService hs, InventoryService is, AllocationService as,
                          DistributionService ds, AuditService au, RfidSerialService rfid, User actor) {
        super("ReliefTrace (prototype) - " + actor);
        this.db = db; this.rfid = rfid; this.actor = actor;
        householdPanel = new HouseholdPanel(hs, actor);
        stockPanel = new StockPanel(is, actor);
        distributionPanel = new DistributionPanel(db, hs, as, ds, actor);
        auditPanel = new AuditPanel(au, db);
        rfid.setListener(distributionPanel);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Dashboard", buildDashboard());
        tabs.addTab("Households", householdPanel);
        tabs.addTab("Stock", stockPanel);
        tabs.addTab("Distribution", distributionPanel);
        tabs.addTab("Audit & Reports", auditPanel);
        tabs.addChangeListener(e -> refreshAll());
        add(tabs);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 560);
        setLocationRelativeTo(null);
        refreshAll();
    }

    private JPanel buildDashboard() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        summary.setFont(summary.getFont().deriveFont(15f));
        p.add(summary, BorderLayout.NORTH);
        return p;
    }

    private void refreshAll() {
        int low = 0;
        synchronized (db) {
            for (var i : db.getItems().values()) if (i.isLowStock()) low++;
            summary.setText("<html>Signed in as <b>" + actor + "</b><br><br>"
                    + "Households: " + db.getHouseholds().size() + "<br>"
                    + "Distributions recorded: " + db.getDistributions().size() + "<br>"
                    + "Low-stock items: " + low + "<br>"
                    + "RFID reader: " + (rfid.isConnected() ? "connected" : "not connected (use manual search or Simulate scan)")
                    + "<br>Audit records: " + db.getAuditRecords().size() + "</html>");
        }
        householdPanel.refresh(); stockPanel.refresh(); distributionPanel.refresh(); auditPanel.refresh();
    }
}
