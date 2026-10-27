package relieftrace.app;

import relieftrace.db.DatabaseManager;
import relieftrace.hardware.RfidSerialService;
import relieftrace.model.User;
import relieftrace.service.*;
import relieftrace.ui.DashboardFrame;
import javax.swing.*;

/** Entry point of the optional prototype (in-memory data, synthetic only). */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DatabaseManager db = new DatabaseManager();
            db.seedSyntheticData();
            AuditService audit = new AuditService(db);
            HouseholdService hs = new HouseholdService(db, audit);
            InventoryService is = new InventoryService(db, audit);
            AllocationService as = new AllocationService(db, audit);
            DistributionService ds = new DistributionService(db, audit);
            RfidSerialService rfid = new RfidSerialService();
            rfid.connect("COM3");                     // prototype: always "not connected"

            User.Role role = (User.Role) JOptionPane.showInputDialog(null, "Demo role (real app uses login):",
                    "ReliefTrace", JOptionPane.QUESTION_MESSAGE, null, User.Role.values(), User.Role.COORDINATOR);
            if (role == null) return;
            User actor = new User(role.ordinal() + 1, role.name().toLowerCase() + "1", role);
            new DashboardFrame(db, hs, is, as, ds, audit, rfid, actor).setVisible(true);
        });
    }
}
