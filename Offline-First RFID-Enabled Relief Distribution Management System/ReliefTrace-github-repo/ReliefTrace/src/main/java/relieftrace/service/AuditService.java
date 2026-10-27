package relieftrace.service;

import relieftrace.db.DatabaseManager;
import relieftrace.model.AuditRecord;
import relieftrace.model.User;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.List;

/** Appends hash-chained audit records and verifies the chain (SHA-256). */
public class AuditService {
    public static final String GENESIS_HASH = "0".repeat(64);
    private final DatabaseManager db;

    public AuditService(DatabaseManager db) { this.db = db; }

    /** Must be called inside the same unit of work as the change it records. */
    public AuditRecord append(String eventType, String entityType, int entityId, String details, User actor) {
        synchronized (db) {
            List<AuditRecord> chain = db.getAuditRecords();
            int seq = chain.size() + 1;
            String prev = chain.isEmpty() ? GENESIS_HASH : chain.get(chain.size() - 1).getRecordHash();
            String time = Instant.now().toString();
            int actorId = actor == null ? 0 : actor.getUserId();
            String hash = computeHash(seq, time, eventType, entityType, entityId, actorId, details, prev);
            AuditRecord r = new AuditRecord(seq, time, eventType, entityType, entityId, actorId, details, prev, hash);
            chain.add(r);
            return r;
        }
    }

    public List<AuditRecord> listAll() { synchronized (db) { return List.copyOf(db.getAuditRecords()); } }

    public VerificationResult verifyChain() {
        synchronized (db) {
            String expectedPrev = GENESIS_HASH;
            int expectedSeq = 1;
            for (AuditRecord r : db.getAuditRecords()) {
                if (r.getSeqNo() != expectedSeq)
                    return new VerificationResult(false, expectedSeq, "Sequence gap or reorder at #" + expectedSeq);
                if (!r.getPrevHash().equals(expectedPrev))
                    return new VerificationResult(false, r.getSeqNo(), "Previous-hash link broken at #" + r.getSeqNo());
                String again = computeHash(r.getSeqNo(), r.getEventTime(), r.getEventType(), r.getEntityType(),
                        r.getEntityId(), r.getActorUserId(), r.getDetails(), r.getPrevHash());
                if (!again.equals(r.getRecordHash()))
                    return new VerificationResult(false, r.getSeqNo(), "Record content changed at #" + r.getSeqNo());
                expectedPrev = r.getRecordHash();
                expectedSeq++;
            }
            return new VerificationResult(true, 0, "Chain intact: " + (expectedSeq - 1) + " record(s) verified.");
        }
    }

    /** DEMO ONLY: simulates someone editing a stored record so verification can be shown to fail. */
    public void tamperForDemo(int seqNo) {
        synchronized (db) {
            for (AuditRecord r : db.getAuditRecords())
                if (r.getSeqNo() == seqNo) r.setDetailsForTamperDemo(r.getDetails() + " [EDITED]");
        }
    }

    /** Length-prefixed fields avoid ambiguity such as "a|b" + "c" versus "a" + "b|c". */
    static String computeHash(int seq, String time, String type, String entityType, int entityId,
                              int actorId, String details, String prevHash) {
        String canonical = f(String.valueOf(seq)) + f(time) + f(type) + f(entityType)
                + f(String.valueOf(entityId)) + f(String.valueOf(actorId)) + f(details) + f(prevHash);
        try {
            byte[] d = MessageDigest.getInstance("SHA-256").digest(canonical.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : d) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) { throw new IllegalStateException(e); }
    }
    private static String f(String v) { return v.length() + ":" + v + ";"; }
}
