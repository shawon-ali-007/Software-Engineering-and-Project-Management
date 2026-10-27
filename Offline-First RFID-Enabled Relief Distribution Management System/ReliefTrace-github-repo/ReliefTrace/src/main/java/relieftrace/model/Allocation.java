package relieftrace.model;

public class Allocation {
    public enum Status { PROPOSED, DISTRIBUTED, REJECTED }

    private final int allocationId;
    private final int roundId;
    private final int householdId;
    private final int itemId;
    private final int quantity;
    private final String ruleExplanation;
    private Status status = Status.PROPOSED;
    private String decisionNote;

    public Allocation(int id, int roundId, int householdId, int itemId, int quantity, String explanation) {
        this.allocationId = id; this.roundId = roundId; this.householdId = householdId;
        this.itemId = itemId; this.quantity = quantity; this.ruleExplanation = explanation;
    }
    public int getAllocationId() { return allocationId; }
    public int getRoundId() { return roundId; }
    public int getHouseholdId() { return householdId; }
    public int getItemId() { return itemId; }
    public int getQuantity() { return quantity; }
    public String getRuleExplanation() { return ruleExplanation; }
    public Status getStatus() { return status; }
    public String getDecisionNote() { return decisionNote; }
    public boolean isProposed() { return status == Status.PROPOSED; }
    public void setStatus(Status s, String note) { this.status = s; this.decisionNote = note; }
}
