-- SYNTHETIC test data only. No real persons. Password hashes are placeholders;
-- create real users through the application's first-run setup.
INSERT INTO app_user(username,password_hash,salt,role) VALUES
 ('coordinator1','PLACEHOLDER','PLACEHOLDER','COORDINATOR'),
 ('volunteer1','PLACEHOLDER','PLACEHOLDER','VOLUNTEER'),
 ('auditor1','PLACEHOLDER','PLACEHOLDER','AUDITOR');
INSERT INTO relief_item(item_name,unit,quantity_on_hand,low_stock_threshold) VALUES
 ('Rice','kg',500,50),('Drinking Water','bottle',600,60),('Blanket','pcs',120,10),('Baby Food Pack','pack',40,5);
INSERT INTO household(household_code,head_name,area_name,member_count,children_under5,elderly_count,damage_level,registered_by) VALUES
 ('HH-0001','Synthetic Head A','Zone-1',5,1,1,'MAJOR',2),
 ('HH-0002','Synthetic Head B','Zone-1',3,0,0,'MINOR',2),
 ('HH-0003','Synthetic Head C','Zone-2',7,2,1,'SEVERE',2);
INSERT INTO rfid_card(uid_hex,household_id,issued_by) VALUES ('04A1B2C3',1,2),('04D4E5F6',2,2);
INSERT INTO distribution_round(round_name,start_date,end_date,status) VALUES ('Round-2026-11-A','2026-11-20','2026-11-27','OPEN');
