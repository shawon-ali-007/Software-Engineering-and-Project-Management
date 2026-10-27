package relieftrace.ui;

import relieftrace.model.*;
import relieftrace.service.InventoryService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/** Relief stock screen: view stock levels and receive stock (Coordinator only). */
public class StockPanel extends JPanel {
    private final InventoryService service;
    private final User actor;
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Item", "Unit", "On hand", "Low-stock level", "Status"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);
    private final JSpinner qty = new JSpinner(new SpinnerNumberModel(10, 1, 100000, 1));

    public StockPanel(InventoryService service, User actor) {
        this.service = service; this.actor = actor;
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton receive = new JButton("Receive stock for selected item");
        receive.setEnabled(actor.canManageStock());
        receive.addActionListener(e -> receive());
        bottom.add(new JLabel("Quantity:")); bottom.add(qty); bottom.add(receive);
        if (!actor.canManageStock()) bottom.add(new JLabel("(read-only for your role)"));
        add(bottom, BorderLayout.SOUTH);
        refresh();
    }

    public void refresh() {
        model.setRowCount(0);
        for (ReliefItem i : service.listItems())
            model.addRow(new Object[]{i.getItemId(), i.getItemName(), i.getUnit(), i.getQuantityOnHand(),
                    i.getLowStockThreshold(), i.isLowStock() ? "LOW" : "OK"});
    }

    private void receive() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select an item first."); return; }
        try {
            service.receiveStock((int) model.getValueAt(row, 0), (int) qty.getValue(), actor);
            refresh();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Cannot complete action", JOptionPane.ERROR_MESSAGE);
        }
    }
}
