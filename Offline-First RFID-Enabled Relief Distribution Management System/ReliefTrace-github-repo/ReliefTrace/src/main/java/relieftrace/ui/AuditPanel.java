package relieftrace.ui;

import relieftrace.db.DatabaseManager;
import relieftrace.model.*;
import relieftrace.service.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;

/** Audit log, chain verification and a simple summary report. */
public class AuditPanel extends JPanel {
    private final AuditService audit;
    private final DatabaseManager db;
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"Seq", "Time (UTC)", "Event", "Entity", "Details", "Hash (first 12)"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);
    private final JLabel result = new JLabel("Not verified yet");
    private final JTextArea report = new JTextArea(6, 40);

    public AuditPanel(AuditService audit, DatabaseManager db) {
        this.audit = audit; this.db = db;
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel south = new JPanel(new BorderLayout(4, 4));
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton verify = new JButton("Verify audit chain");
        JButton tamper = new JButton("DEMO: tamper with selected row");
        JButton summary = new JButton("Summary report");
        verify.addActionListener(e -> verify());
        tamper.addActionListener(e -> tamper());
        summary.addActionListener(e -> summary());
        buttons.add(verify); buttons.add(tamper); buttons.add(summary); buttons.add(result);
        report.setEditable(false);
        south.add(buttons, BorderLayout.NORTH);
        south.add(new JScrollPane(report), BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);
        refresh();
    }

    public void refresh() {
        model.setRowCount(0);
        for (AuditRecord r : audit.listAll())
            model.addRow(new Object[]{r.getSeqNo(), r.getEventTime(), r.getEventType(),
                    r.getEntityType() + "#" + r.getEntityId(), r.getDetails(), r.getRecordHash().substring(0, 12)});
    }

    private void verify() {
        VerificationResult v = audit.verifyChain();
        result.setText((v.isValid() ? "VALID: " : "BROKEN: ") + v.getMessage());
        result.setForeground(v.isValid() ? new Color(0, 110, 0) : Color.RED);
    }

    private void tamper() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a record row first."); return; }
        audit.tamperForDemo((int) model.getValueAt(row, 0));
        refresh();
    }

    private void summary() {
        Map<String, Integer> totals = new TreeMap<>();
        synchronized (db) {
            for (Distribution d : db.getDistributions().values()) {
                Allocation a = db.getAllocations().get(d.getAllocationId());
                ReliefItem i = db.getItems().get(a.getItemId());
                totals.merge(i.getItemName() + " (" + i.getUnit() + ")", d.getQuantityGiven(), Integer::sum);
            }
            StringBuilder sb = new StringBuilder("Distributed totals (this session):\n");
            if (totals.isEmpty()) sb.append("  none yet\n");
            totals.forEach((k, v) -> sb.append("  ").append(k).append(": ").append(v).append('\n'));
            sb.append("Households registered: ").append(db.getHouseholds().size());
            report.setText(sb.toString());
        }
    }
}
