// ReliefTrace - ESP32 + RC522 UID reader (DESIGN SAMPLE, not yet bench-tested by the author)
// Library: "MFRC522" (Arduino Library Manager). Board: ESP32 Dev Module.
// Wiring (RC522 -> ESP32): SDA/SS -> GPIO5, SCK -> GPIO18, MOSI -> GPIO23, MISO -> GPIO19,
//                          RST -> GPIO22, GND -> GND, 3.3V -> 3V3  (RC522 is a 3.3 V device; never 5 V)
// Serial protocol (115200 baud, newline-terminated ASCII):
//   READY            sent once at start-up
//   UID:<HEX>        sent when a card is read, e.g. UID:04A1B2C3 (uppercase hex, no separators)
#include <SPI.h>
#include <MFRC522.h>

#define SS_PIN  5
#define RST_PIN 22
MFRC522 rfid(SS_PIN, RST_PIN);

String lastUid = "";
unsigned long lastSentMs = 0;
const unsigned long DEBOUNCE_MS = 2000;   // ignore the same card for 2 s

void setup() {
  Serial.begin(115200);
  SPI.begin(18, 19, 23, SS_PIN);          // SCK, MISO, MOSI, SS
  rfid.PCD_Init();
  Serial.println("READY");
}

void loop() {
  if (!rfid.PICC_IsNewCardPresent() || !rfid.PICC_ReadCardSerial()) return;

  String uid = "";
  for (byte i = 0; i < rfid.uid.size; i++) {
    if (rfid.uid.uidByte[i] < 0x10) uid += "0";
    uid += String(rfid.uid.uidByte[i], HEX);
  }
  uid.toUpperCase();

  if (uid != lastUid || millis() - lastSentMs > DEBOUNCE_MS) {
    Serial.print("UID:");
    Serial.println(uid);
    lastUid = uid;
    lastSentMs = millis();
  }
  rfid.PICC_HaltA();
  rfid.PCD_StopCrypto1();
}
