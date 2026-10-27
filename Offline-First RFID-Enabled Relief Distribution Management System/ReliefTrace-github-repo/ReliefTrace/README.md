# ReliefTrace

**An Offline-First, RFID-Enabled Relief Distribution Management System**
Individual Software Engineering project - Department of ICT, Mawlana Bhashani Science and Technology University (MBSTU), Bangladesh.

ReliefTrace helps authorised staff at a disaster relief centre register households, identify them by RFID card (or manual search), recommend relief quantities with transparent rules, record handovers after **human approval**, keep stock consistent, and verify a **SHA-256 hash-chained audit log**. It works offline on one Windows computer.

> **Status:** design and implementation plan complete; a small in-memory Swing prototype is included. The full SQLite + ESP32 system is **planned, not finished**. See the status table below. Synthetic data only.

## Status

| Part | State |
|---|---|
| Full assignment report (proposal, process model, Gantt, 6 diagrams, GUI design, tests plan) | Done: `docs/ReliefTrace_Assignment.md` |
| SQLite schema and synthetic seed | Written; constraint probes passed in SQLite |
| Swing prototype (in-memory data, simulated card scan) | Compiles; service-layer smoke test passed; GUI not yet screenshot-verified |
| SQLite/JDBC persistence | Planned |
| ESP32 + RC522 serial integration | Sketch written, **not bench-tested** |
| System test results | Not yet run |

## Features (planned vs prototype)

Household registration and search - RFID card mapping - stock and ledger - rule-based explainable proposals - transactional approval and duplicate prevention - hash-chain audit verification - summary reports - manual lookup fallback.

## Repository layout

```text
docs/        report (Markdown), diagrams, screenshots
database/    schema.sql, seed_synthetic.sql
hardware/    esp32_rc522_reader sketch
src/main/java/relieftrace/{app,model,db,service,hardware,ui}
```

## Build and run the prototype (JDK 17+)

```bash
javac -d out $(find src -name "*.java")
java -cp out relieftrace.app.Main
```
On Windows: `dir /s /b src\*.java > files.txt` then `javac -d out @files.txt`.
Choose a role at start. Use **Distribution > Simulate card scan** (UID `04A1B2C3`) or **Manual search**.

## Hardware (design)

ESP32 + RC522 over SPI; prints `UID:<HEX>` lines at 115200 baud over USB Serial. RC522 is 3.3 V only. Card compatibility must be tested before relying on it. See report Section 12.

## Security, privacy and limitations

- RFID UID is an identifier, **not authentication**; a human approves every distribution.
- Hash chaining detects later modification; it does not prove data was true or the allocation fair.
- SQLite file is unencrypted; use OS protection. Never commit databases, backups or real personal data.

## Author

Shawon - IT24051 - ICT, MBSTU. Course: `[course]`. Teacher: `[name]`.
