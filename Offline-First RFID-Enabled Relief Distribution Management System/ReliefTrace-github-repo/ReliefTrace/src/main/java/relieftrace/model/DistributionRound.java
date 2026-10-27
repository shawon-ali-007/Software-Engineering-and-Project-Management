package relieftrace.model;

import java.time.LocalDate;

public class DistributionRound {
    private final int roundId;
    private final String roundName;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private String status;   // PLANNED, OPEN, CLOSED

    public DistributionRound(int id, String name, LocalDate start, LocalDate end, String status) {
        this.roundId = id; this.roundName = name; this.startDate = start; this.endDate = end; this.status = status;
    }
    public int getRoundId() { return roundId; }
    public String getRoundName() { return roundName; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public String getStatus() { return status; }
    public boolean isOpen() { return "OPEN".equals(status); }
    @Override public String toString() { return roundName + " [" + status + "]"; }
}
