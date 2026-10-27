package relieftrace.hardware;

public interface CardScanListener {
    /** Called when a valid UID line has been received from the reader (may arrive on a non-UI thread). */
    void onCardScanned(String uidHex);
}
