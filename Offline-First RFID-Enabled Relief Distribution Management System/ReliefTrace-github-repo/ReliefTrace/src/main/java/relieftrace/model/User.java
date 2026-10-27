package relieftrace.model;

/** Application user with a role (entity class). */
public class User {
    public enum Role { COORDINATOR, VOLUNTEER, AUDITOR }

    private final int userId;
    private final String username;
    private final Role role;

    public User(int userId, String username, Role role) {
        this.userId = userId; this.username = username; this.role = role;
    }
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public Role getRole() { return role; }
    public boolean canApproveDistribution() { return role != Role.AUDITOR; }
    public boolean canRegisterHouseholds() { return role != Role.AUDITOR; }
    public boolean canManageStock() { return role == Role.COORDINATOR; }
    @Override public String toString() { return username + " (" + role + ")"; }
}
