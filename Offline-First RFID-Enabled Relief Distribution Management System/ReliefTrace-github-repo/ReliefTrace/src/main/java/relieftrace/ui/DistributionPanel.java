package relieftrace.ui;

import relieftrace.db.DatabaseManager;
import relieftrace.hardware.CardScanListener;
import relieftrace.model.*;
import relieftrace.service.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/** Allocation and distribution screen: identify household (RFID or manual), review, approve or reject. */
public class DistributionPanel extends JPanel implements CardScanListener {
    private final DatabaseManager db;
    private final HouseholdService households;
    private final AllocationService allocations;
    private final DistributionService distributions;
    private final User actor;

    private Household selected;
    private String selectedCardUid;                 // null when found manually
    private final JLabel householdLabel = new JLabel("No household selected");
    private final JTextField uidField = new JTextField("04A1B2C3", 10);
    private final JTextField searchField = new JTextField(10);
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Item", "Qty", "Status", "Explanation"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);

    public DistributionPanel(DatabaseManager db, HouseholdService households, AllocationService allocations,
                             DistributionService distributions, User actor) {
        this.db = db; this.households = households; this.allocations = allocations;
        this.distributions = distributions; this.actor = actor;
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel top = new JPanel(new GridLayout(3, 1));
        JPanel scanRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton sim = new JButton("Simulate card scan");
        sim.addActionListener(e -> onCardScanned(uidField.getText()));
        scanRow.add(new JLabel("Card UID:")); scanRow.add(uidField); scanRow.add(sim);
        JPanel manualRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton find = new JButton("Manual search");
        find.addActionListener(e -> manualSearch());
        manualRow.add(new JLabel("Manual (code/name):")); manualRow.add(searchField); manualRow.add(find);
        top.add(scanRow); top.add(manualRow); top.add(householdLabel);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton gen = new JButton("Generate proposals");
        JButton approve = new JButton("Approve & record");
        JButton reject = new JButton("Reject selected");
        gen.addActionListener(e -> generate());
        approve.addActionListener(e -> approve());
        reject.addActionListener(e -> reject());
        approve.setEnabled(actor.canApproveDistribution());
        reject.setEnabled(actor.canApproveDistribution());
        gen.setEnabled(actor.canApproveDistribution());
        bottom.add(gen); bottom.add(approve); bottom.add(reject);
        add(bottom, BorderLayout.SOUTH);
    }

    @Override public void onCardScanned(String uidHex) {
        SwingUtilities.invokeLater(() -> {
            Household h = households.findByCardUid(uidHex);
            if (h == null) {
                householdLabel.setText("Unknown, revoked or inactive card. Use manual search.");
                selected = null; selectedCardUid = null; model.setRowCount(0);
            } else select(h, uidHex.trim().toUpperCase());
        });
    }

    private void manualSearch() {
        List<Household> found = households.search(searchField.getText());
        if (found.isEmpty()) { householdLabel.setText("No household found."); return; }
        Household h = found.size() == 1 ? found.get(0) : (Household) JOptionPane.showInputDialog(this,
                "Select household", "Manual search", JOptionPane.PLAIN_MESSAGE, null, found.toArray(), found.get(0));
        if (h != null) select(h, null);
    }

    private void select(Household h, String uid) {
        selected = h; selectedCardUid = uid;
        householdLabel.setText("Household: " + h + " | members " + h.getMemberCount()
                + " | identified by " + (uid == null ? "MANUAL search" : "RFID " + uid));
        refresh();
    }

    public void refresh() {
        model.setRowCount(0);
        DistributionRound r = openRound();
        if (selected == null || r == null) return;
        for (Allocation a : allocations.findFor(selected.getHouseholdId(), r.getRoundId()))
            model.addRow(new Object[]{a.getAllocationId(), db.getItems().get(a.getItemId()).getItemName(),
                    a.getQuantity(), a.getStatus(), a.getRuleExplanation()});
    }

    private DistributionRound openRound() {
        for (DistributionRound r : db.getRounds().values()) if (r.isOpen()) return r;
        return null;
    }

    private void generate() {
        if (selected == null) { householdLabel.setText("Identify a household first."); return; }
        try {
            DistributionRound r = openRound();
            if (r == null) throw new IllegalStateException("No open distribution round");
            allocations.generateProposals(selected.getHouseholdId(), r.getRoundId(), actor);
            refresh();
        } catch (RuntimeException ex) { error(ex); }
    }

    private void approve() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a proposal row."); return; }
        try {
            String method = selectedCardUid == null ? "MANUAL" : "RFID";
            distributions.approveAndRecord((int) model.getValueAt(row, 0), method, selectedCardUid, actor);
            refresh();
        } catch (RuntimeException ex) { error(ex); refresh(); }
    }

    private void reject() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a proposal row."); return; }
        String reason = JOptionPane.showInputDialog(this, "Reason for rejection:");
        if (reason == null) return;
        try { distributions.rejectAllocation((int) model.getValueAt(row, 0), reason, actor); refresh(); }
        catch (RuntimeException ex) { error(ex); }
    }

    private void error(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Cannot complete action", JOptionPane.ERROR_MESSAGE);
    }
}
