package relieftrace.ui;

import relieftrace.model.*;
import relieftrace.service.HouseholdService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/** Household registration, search and card assignment screen. */
public class HouseholdPanel extends JPanel {
    private final HouseholdService service;
    private final User actor;
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"Code", "Head", "Area", "Members", "<5 yrs", "Elderly", "Damage"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);
    private final JTextField searchField = new JTextField(14);
    private final JTextField head = new JTextField(10), area = new JTextField(8);
    private final JSpinner members = new JSpinner(new SpinnerNumberModel(4, 1, 30, 1));
    private final JSpinner children = new JSpinner(new SpinnerNumberModel(0, 0, 30, 1));
    private final JSpinner elderly = new JSpinner(new SpinnerNumberModel(0, 0, 30, 1));
    private final JComboBox<String> damage = new JComboBox<>(new String[]{"MINOR", "MAJOR", "SEVERE"});
    private final JTextField uidField = new JTextField(10);

    public HouseholdPanel(HouseholdService service, User actor) {
        this.service = service; this.actor = actor;
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> refresh());
        top.add(new JLabel("Search (code / name / area):")); top.add(searchField); top.add(searchBtn);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        form.setBorder(BorderFactory.createTitledBorder("Register household (synthetic data only in demos)"));
        form.add(new JLabel("Head:")); form.add(head);
        form.add(new JLabel("Area:")); form.add(area);
        form.add(new JLabel("Members:")); form.add(members);
        form.add(new JLabel("<5 yrs:")); form.add(children);
        form.add(new JLabel("Elderly:")); form.add(elderly);
        form.add(damage);
        JButton reg = new JButton("Register");
        reg.addActionListener(e -> register());
        form.add(reg);
        form.add(new JLabel("Card UID:")); form.add(uidField);
        JButton assign = new JButton("Assign card to selected");
        assign.addActionListener(e -> assignCard());
        form.add(assign);
        add(form, BorderLayout.SOUTH);
        refresh();
    }

    public void refresh() {
        model.setRowCount(0);
        for (Household h : service.search(searchField.getText()))
            model.addRow(new Object[]{h.getHouseholdCode(), h.getHeadName(), h.getAreaName(), h.getMemberCount(),
                    h.getChildrenUnder5(), h.getElderlyCount(), h.getDamageLevel()});
    }

    private void register() {
        try {
            service.register(head.getText(), area.getText(), (int) members.getValue(), (int) children.getValue(),
                    (int) elderly.getValue(), (String) damage.getSelectedItem(), actor);
            head.setText(""); refresh();
        } catch (RuntimeException ex) { error(ex); }
    }

    private void assignCard() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a household first."); return; }
        try {
            String code = (String) model.getValueAt(row, 0);
            Household h = service.search(code).get(0);
            service.assignCard(h.getHouseholdId(), uidField.getText(), actor);
            JOptionPane.showMessageDialog(this, "Card assigned to " + code);
        } catch (RuntimeException ex) { error(ex); }
    }
    private void error(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Cannot complete action", JOptionPane.ERROR_MESSAGE);
    }
}
