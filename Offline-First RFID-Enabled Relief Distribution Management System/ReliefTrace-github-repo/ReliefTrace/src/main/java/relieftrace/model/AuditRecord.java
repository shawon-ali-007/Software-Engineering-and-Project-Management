package relieftrace.model;

public class AuditRecord {
    private final int seqNo;
    private final String eventTime;
    private final String eventType;
    private final String entityType;
    private final int entityId;
    private final int actorUserId;
    private String details;                 // mutable ONLY so the tamper demo can modify it
    private final String prevHash;
    private final String recordHash;

    public AuditRecord(int seqNo, String eventTime, String eventType, String entityType, int entityId,
                       int actorUserId, String details, String prevHash, String recordHash) {
        this.seqNo = seqNo; this.eventTime = eventTime; this.eventType = eventType; this.entityType = entityType;
        this.entityId = entityId; this.actorUserId = actorUserId; this.details = details;
        this.prevHash = prevHash; this.recordHash = recordHash;
    }
    public int getSeqNo() { return seqNo; }
    public String getEventTime() { return eventTime; }
    public String getEventType() { return eventType; }
    public String getEntityType() { return entityType; }
    public int getEntityId() { return entityId; }
    public int getActorUserId() { return actorUserId; }
    public String getDetails() { return details; }
    public String getPrevHash() { return prevHash; }
    public String getRecordHash() { return recordHash; }
    public void setDetailsForTamperDemo(String d) { this.details = d; }
}
