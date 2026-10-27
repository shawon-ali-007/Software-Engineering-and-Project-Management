package relieftrace.model;

public class Household {
    private final int householdId;
    private final String householdCode;
    private final String headName;
    private final String areaName;
    private final int memberCount;
    private final int childrenUnder5;
    private final int elderlyCount;
    private final String damageLevel;
    private String status = "ACTIVE";

    public Household(int id, String code, String head, String area, int members,
                     int childrenUnder5, int elderly, String damage) {
        this.householdId = id; this.householdCode = code; this.headName = head; this.areaName = area;
        this.memberCount = members; this.childrenUnder5 = childrenUnder5;
        this.elderlyCount = elderly; this.damageLevel = damage;
    }
    public int getHouseholdId() { return householdId; }
    public String getHouseholdCode() { return householdCode; }
    public String getHeadName() { return headName; }
    public String getAreaName() { return areaName; }
    public int getMemberCount() { return memberCount; }
    public int getChildrenUnder5() { return childrenUnder5; }
    public int getElderlyCount() { return elderlyCount; }
    public String getDamageLevel() { return damageLevel; }
    public String getStatus() { return status; }
    public boolean isActive() { return "ACTIVE".equals(status); }
    @Override public String toString() { return householdCode + " - " + headName + " (" + areaName + ")"; }
}
