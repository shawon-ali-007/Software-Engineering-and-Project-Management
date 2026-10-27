package relieftrace.service;

/** Result of an audit-chain verification run. */
public class VerificationResult {
    private final boolean valid;
    private final int brokenAtSeq;     // 0 when valid
    private final String message;

    public VerificationResult(boolean valid, int brokenAtSeq, String message) {
        this.valid = valid; this.brokenAtSeq = brokenAtSeq; this.message = message;
    }
    public boolean isValid() { return valid; }
    public int getBrokenAtSeq() { return brokenAtSeq; }
    public String getMessage() { return message; }
}
