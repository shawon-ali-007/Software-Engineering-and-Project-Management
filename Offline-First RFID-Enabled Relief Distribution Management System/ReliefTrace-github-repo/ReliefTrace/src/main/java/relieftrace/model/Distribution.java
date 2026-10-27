package relieftrace.model;

import java.time.Instant;

public class Distribution {
    private final int distributionId;
    private final int allocationId;
    private final int approvedByUserId;
    private final String inputMethod;     // RFID or MANUAL
    private final String cardUid;         // null when MANUAL
    private final int quantityGiven;
    private final Instant distributedAt = Instant.now();
    private final String remarks;

    public Distribution(int id, int allocationId, int approvedBy, String inputMethod,
                        String cardUid, int quantityGiven, String remarks) {
        this.distributionId = id; this.allocationId = allocationId; this.approvedByUserId = approvedBy;
        this.inputMethod = inputMethod; this.cardUid = cardUid; this.quantityGiven = quantityGiven; this.remarks = remarks;
    }
    public int getDistributionId() { return distributionId; }
    public int getAllocationId() { return allocationId; }
    public int getApprovedByUserId() { return approvedByUserId; }
    public String getInputMethod() { return inputMethod; }
    public String getCardUid() { return cardUid; }
    public int getQuantityGiven() { return quantityGiven; }
    public Instant getDistributedAt() { return distributedAt; }
    public String getRemarks() { return remarks; }
}
