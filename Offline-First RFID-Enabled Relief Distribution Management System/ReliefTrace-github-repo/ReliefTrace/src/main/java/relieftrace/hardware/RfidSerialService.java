package relieftrace.hardware;

import java.util.regex.Pattern;

/**
 * Prototype stub of the USB Serial reader service.
 * The real design opens a COM port at 115200 baud (for example with the jSerialComm library),
 * reads lines such as "UID:04A1B2C3" and calls the listener. connect() here always reports
 * "not connected", which exercises the manual-lookup fallback. simulateScan() lets the demo
 * inject a UID without hardware.
 */
public class RfidSerialService {
    private static final Pattern UID_LINE = Pattern.compile("UID:([0-9A-F]{8}|[0-9A-F]{14}|[0-9A-F]{20})");
    private CardScanListener listener;
    private boolean connected = false;

    public void setListener(CardScanListener l) { this.listener = l; }
    public boolean connect(String portName) { connected = false; return connected; }   // real: open serial port
    public void disconnect() { connected = false; }
    public boolean isConnected() { return connected; }

    /** Parses one line from the reader; ignores anything that is not a well-formed UID line. */
    public void handleLine(String line) {
        var m = UID_LINE.matcher(line == null ? "" : line.trim().toUpperCase());
        if (m.matches() && listener != null) listener.onCardScanned(m.group(1));
    }
    public void simulateScan(String uidHex) { handleLine("UID:" + uidHex); }
}
