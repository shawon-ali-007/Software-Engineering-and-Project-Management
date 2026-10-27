# ReliefTrace

### An Offline-First, RFID-Enabled Relief Distribution Management System

<p align="center">
  <strong>Individual Software Engineering Project</strong><br>
  Department of Information and Communication Technology (ICT)<br>
  Mawlana Bhashani Science and Technology University (MBSTU)<br>
  Bangladesh
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Language-Java-orange" alt="Java">
  <img src="https://img.shields.io/badge/Interface-Java%20Swing-blue" alt="Java Swing">
  <img src="https://img.shields.io/badge/Database-SQLite-003B57" alt="SQLite">
  <img src="https://img.shields.io/badge/Hardware-ESP32%20%7C%20RC522-green" alt="ESP32 and RC522">
  <img src="https://img.shields.io/badge/Status-Prototype-yellow" alt="Prototype">
</p>

---

## 1. Project Overview

**ReliefTrace** is an offline-first desktop application designed to improve transparency, accountability, and consistency in disaster relief distribution.

The system helps authorised relief-centre staff register households, associate RFID cards with household records, manage relief inventory, recommend relief quantities using transparent rules, and record distributions after human approval.

It also incorporates duplicate-distribution prevention and a SHA-256 hash-chained audit log to help detect subsequent changes to recorded events.

The application is designed to operate on a single Windows computer without requiring an internet connection for its core workflow.

## 2. Academic Information

| Field | Details |
|---|---|
| Student Name | **MD Shawon Ali** |
| Student ID | **IT24051** |
| Department | Information and Communication Technology (ICT) |
| University | Mawlana Bhashani Science and Technology University (MBSTU) |
| Course Title | Software Engineering and Project Management |
| Course Code | **ICT-3108** |
| Project Type | Individual Academic Project |

## 3. Project Objectives

- Maintain organised household and relief-distribution records.
- Support household identification through RFID cards and manual search.
- Manage available relief stock and distribution transactions.
- Recommend relief quantities using explainable, rule-based criteria.
- Require human approval before confirming a relief handover.
- Prevent duplicate distributions within the applicable distribution round.
- Maintain an auditable record using SHA-256 hash chaining.
- Support offline operation and manual lookup when RFID is unavailable.

## 4. Key Features

| Feature | Description |
|---|---|
| Household Management | Register and search household records. |
| RFID Identification | Associate cards with households and simulate card scans in the current prototype. |
| Inventory Management | Maintain relief-stock records and quantities. |
| Explainable Allocation | Generate rule-based relief recommendations for staff review. |
| Distribution Management | Record approved handovers and check for duplicate distributions. |
| Audit Verification | Verify the integrity of a SHA-256 hash-chained audit log. |
| Summary Reports | Support distribution and inventory reporting. |
| Offline Operation | Designed for use on a local Windows computer. |

## 5. Technology Stack

- **Programming language:** Java
- **Desktop interface:** Java Swing
- **Database:** SQLite (planned integration)
- **Hardware:** ESP32 development board and RC522 RFID reader
- **Hardware communication:** USB Serial, 115200 baud
- **Audit integrity:** SHA-256 hash chaining
- **Development environment:** JDK 17 or later

## 6. System Architecture

The intended system follows this workflow:

```text
RFID Card / Manual Search
           |
           v
     ESP32 + RC522
           |
      USB Serial
           |
           v
     Java Swing UI
           |
           v
   Service / Business Logic
           |
           v
      SQLite Database
           |
           v
 Distribution Records
           |
           v
 SHA-256 Audit Verification
```

The RFID hardware integration and SQLite persistence are planned components and have not yet been fully integrated into the application.

## 7. Repository Structure

```text
ReliefTrace/
├── README.md
├── .gitignore
├── docs/
│   ├── ReliefTrace_Assignment.md
│   ├── diagrams/
│   └── screenshots/
├── database/
│   ├── schema.sql
│   └── seed_synthetic.sql
├── hardware/
│   └── esp32_rc522_reader/
└── src/
    └── main/
        └── java/
            └── relieftrace/
                ├── app/
                ├── model/
                ├── db/
                ├── service/
                ├── hardware/
                └── ui/
```

*The tree above describes the intended repository layout. Folder names and files should match the actual repository contents.*

## 8. Implementation Status

This repository contains the project proposal, design documentation, database schema, and a small Java Swing prototype. The complete system is still under development.

| Component | Current Status |
|---|---|
| Project proposal and assignment report | Completed |
| Software process model and project schedule | Documented |
| Gantt chart and project milestones | Documented |
| Six required diagrams | Included in the report |
| SQLite schema and synthetic seed | Written; constraint probes passed |
| Java Swing prototype | Compiles; service-layer smoke test passed |
| GUI screenshot verification | Pending |
| SQLite/JDBC persistence | Planned |
| ESP32–RC522 serial integration | Sketch written; hardware testing pending |
| Complete system testing | Pending |

**Important:** The prototype uses in-memory data and a simulated RFID scan. The full SQLite-backed and hardware-integrated system is not yet complete.

## 9. How to Run the Prototype

### Requirements

- JDK 17 or later
- A terminal or command prompt
- The project source files

### Windows

Open Command Prompt in the project root directory and run:

```bat
dir /s /b src\*.java > files.txt
javac -d out @files.txt
java -cp out relieftrace.app.Main
```

If compilation succeeds, the application should launch.

In the prototype, choose a role at startup and navigate to **Distribution → Simulate card scan** to test the simulated UID `04A1B2C3`, or use **Manual Search**.

These instructions describe the current prototype, not the planned SQLite or physical RFID functionality.

## 10. Hardware Design

The planned hardware setup consists of an ESP32 development board, an RC522 RFID reader, and compatible RFID cards.

The reader communicates with the ESP32 using SPI. The ESP32 is intended to transmit card UIDs over USB Serial in the following format:

```text
UID:04A1B2C3
```

The intended serial baud rate is **115200**.

**Hardware precautions:**
- The RC522 operates at 3.3 V; do not connect its power input to 5 V.
- Confirm that the RFID card frequency and protocol are compatible with the RC522.
- Physical hardware operation must be tested before claiming successful integration.

## 11. Security, Privacy, and Limitations

- An RFID UID is an identifier, not secure authentication.
- Every distribution requires human approval.
- Hash chaining can help detect subsequent record modification but cannot prove that the original data was truthful or that an allocation was fair.
- The planned SQLite database is not encrypted by default and must be protected using appropriate operating-system access controls.
- Only synthetic or non-sensitive demonstration data should be committed to this public repository.
- Database files, backups, passwords, credentials, and real household personal information must not be uploaded.

## 12. Project Documentation

The assignment report is available at:

[`docs/ReliefTrace_Assignment.md`](docs/ReliefTrace_Assignment.md)

It covers the project proposal, software process model, completion schedule, milestones, deliverables, Gantt chart, required diagrams, GUI design, and testing plan.

## 13. Future Improvements

- Complete SQLite/JDBC integration.
- Connect the ESP32 and RC522 reader to the Java application.
- Test real RFID cards and serial communication.
- Complete transaction-level inventory updates and duplicate-prevention testing.
- Verify audit-log behaviour through automated tests.
- Capture screenshots of the running application.
- Conduct end-to-end testing and improve error handling.

## 14. Author

**MD Shawon Ali**  
Student ID: IT24051  
Department of ICT  
Mawlana Bhashani Science and Technology University (MBSTU)  
Course: Software Engineering and Project Management  
Course Code: ICT-3108

---

*This repository is an individual academic project developed for educational purposes. Implementation status is reported transparently; planned features should not be interpreted as completed functionality.*
