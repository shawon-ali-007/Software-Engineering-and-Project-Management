-- ReliefTrace SQLite schema (design). Run once on an empty database.
PRAGMA foreign_keys = ON;

CREATE TABLE app_user (
  user_id        INTEGER PRIMARY KEY AUTOINCREMENT,
  username       TEXT NOT NULL UNIQUE,
  password_hash  TEXT NOT NULL,            -- PBKDF2 hash, never plain text
  salt           TEXT NOT NULL,
  role           TEXT NOT NULL CHECK (role IN ('COORDINATOR','VOLUNTEER','AUDITOR')),
  is_active      INTEGER NOT NULL DEFAULT 1 CHECK (is_active IN (0,1))
);

CREATE TABLE household (
  household_id    INTEGER PRIMARY KEY AUTOINCREMENT,
  household_code  TEXT NOT NULL UNIQUE,     -- e.g. HH-0001
  head_name       TEXT NOT NULL CHECK (length(trim(head_name)) > 0),
  area_name       TEXT NOT NULL,
  phone           TEXT,
  member_count    INTEGER NOT NULL CHECK (member_count BETWEEN 1 AND 30),
  children_under5 INTEGER NOT NULL DEFAULT 0 CHECK (children_under5 >= 0),
  elderly_count   INTEGER NOT NULL DEFAULT 0 CHECK (elderly_count >= 0),
  damage_level    TEXT NOT NULL CHECK (damage_level IN ('MINOR','MAJOR','SEVERE')),
  status          TEXT NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE','INACTIVE')),
  registered_by   INTEGER NOT NULL REFERENCES app_user(user_id),
  registered_at   TEXT NOT NULL DEFAULT (datetime('now')),
  CHECK (children_under5 + elderly_count <= member_count)
);

CREATE TABLE rfid_card (
  card_id      INTEGER PRIMARY KEY AUTOINCREMENT,
  uid_hex      TEXT NOT NULL UNIQUE
               CHECK (length(uid_hex) IN (8,14,20) AND uid_hex NOT GLOB '*[^0-9A-F]*'),
  household_id INTEGER NOT NULL REFERENCES household(household_id),
  status       TEXT NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE','REVOKED','LOST')),
  issued_by    INTEGER NOT NULL REFERENCES app_user(user_id),
  issued_at    TEXT NOT NULL DEFAULT (datetime('now'))
);
-- At most one ACTIVE card per household
CREATE UNIQUE INDEX ux_one_active_card ON rfid_card(household_id) WHERE status = 'ACTIVE';

CREATE TABLE relief_item (
  item_id             INTEGER PRIMARY KEY AUTOINCREMENT,
  item_name           TEXT NOT NULL UNIQUE,
  unit                TEXT NOT NULL,
  quantity_on_hand    INTEGER NOT NULL DEFAULT 0 CHECK (quantity_on_hand >= 0),  -- never negative
  low_stock_threshold INTEGER NOT NULL DEFAULT 0 CHECK (low_stock_threshold >= 0)
);

CREATE TABLE distribution_round (
  round_id    INTEGER PRIMARY KEY AUTOINCREMENT,
  round_name  TEXT NOT NULL UNIQUE,
  start_date  TEXT NOT NULL,
  end_date    TEXT NOT NULL,
  status      TEXT NOT NULL DEFAULT 'PLANNED' CHECK (status IN ('PLANNED','OPEN','CLOSED')),
  CHECK (end_date >= start_date)
);

CREATE TABLE allocation (
  allocation_id    INTEGER PRIMARY KEY AUTOINCREMENT,
  round_id         INTEGER NOT NULL REFERENCES distribution_round(round_id),
  household_id     INTEGER NOT NULL REFERENCES household(household_id),
  item_id          INTEGER NOT NULL REFERENCES relief_item(item_id),
  quantity         INTEGER NOT NULL CHECK (quantity > 0),
  rule_explanation TEXT NOT NULL,
  status           TEXT NOT NULL DEFAULT 'PROPOSED' CHECK (status IN ('PROPOSED','DISTRIBUTED','REJECTED')),
  decision_note    TEXT,
  created_at       TEXT NOT NULL DEFAULT (datetime('now')),
  UNIQUE (round_id, household_id, item_id)   -- one allocation per household, item and round
);

CREATE TABLE distribution (
  distribution_id INTEGER PRIMARY KEY AUTOINCREMENT,
  allocation_id   INTEGER NOT NULL UNIQUE REFERENCES allocation(allocation_id), -- one handover per allocation
  approved_by     INTEGER NOT NULL REFERENCES app_user(user_id),
  input_method    TEXT NOT NULL CHECK (input_method IN ('RFID','MANUAL')),
  card_id         INTEGER REFERENCES rfid_card(card_id),
  quantity_given  INTEGER NOT NULL CHECK (quantity_given > 0),
  distributed_at  TEXT NOT NULL DEFAULT (datetime('now')),
  remarks         TEXT,
  CHECK (input_method <> 'RFID' OR card_id IS NOT NULL)
);

CREATE TABLE inventory_transaction (
  txn_id          INTEGER PRIMARY KEY AUTOINCREMENT,
  item_id         INTEGER NOT NULL REFERENCES relief_item(item_id),
  txn_type        TEXT NOT NULL CHECK (txn_type IN ('RECEIVE','DISTRIBUTE','ADJUST')),
  quantity_change INTEGER NOT NULL CHECK (quantity_change <> 0),
  distribution_id INTEGER REFERENCES distribution(distribution_id),
  note            TEXT,
  created_by      INTEGER NOT NULL REFERENCES app_user(user_id),
  created_at      TEXT NOT NULL DEFAULT (datetime('now')),
  CHECK ((txn_type = 'DISTRIBUTE') = (distribution_id IS NOT NULL))
);

CREATE TABLE audit_record (
  seq_no        INTEGER PRIMARY KEY,         -- 1,2,3,... assigned inside the transaction
  event_time    TEXT NOT NULL,
  event_type    TEXT NOT NULL,
  entity_type   TEXT NOT NULL,
  entity_id     INTEGER,
  actor_user_id INTEGER REFERENCES app_user(user_id),
  details       TEXT NOT NULL,
  prev_hash     TEXT NOT NULL CHECK (length(prev_hash) = 64),
  record_hash   TEXT NOT NULL UNIQUE CHECK (length(record_hash) = 64)
);

-- Defence in depth (not a security boundary: a person with file access can drop triggers)
CREATE TRIGGER trg_audit_no_update BEFORE UPDATE ON audit_record
BEGIN SELECT RAISE(ABORT, 'audit_record is append-only'); END;
CREATE TRIGGER trg_audit_no_delete BEFORE DELETE ON audit_record
BEGIN SELECT RAISE(ABORT, 'audit_record is append-only'); END;

CREATE TRIGGER trg_distribution_requires_proposed BEFORE INSERT ON distribution
BEGIN
  SELECT CASE WHEN (SELECT status FROM allocation WHERE allocation_id = NEW.allocation_id) <> 'PROPOSED'
         THEN RAISE(ABORT, 'allocation is not in PROPOSED state') END;
END;

CREATE INDEX ix_household_name ON household(head_name);
CREATE INDEX ix_allocation_household ON allocation(household_id, round_id);
