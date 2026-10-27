package relieftrace.model;

public class RfidCard {
    private final int cardId;
    private final String uidHex;
    private final int householdId;
    private String status = "ACTIVE";

    public RfidCard(int cardId, String uidHex, int householdId) {
        this.cardId = cardId; this.uidHex = uidHex; this.householdId = householdId;
    }
    public int getCardId() { return cardId; }
    public String getUidHex() { return uidHex; }
    public int getHouseholdId() { return householdId; }
    public String getStatus() { return status; }
    public boolean isActive() { return "ACTIVE".equals(status); }
}
