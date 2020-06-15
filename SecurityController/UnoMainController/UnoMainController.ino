/*****************************************************************************************************/
/* We use a Robodyn ATmega2560+ESP8266                                                               */
/* This code is to upload using the ATMega2560 as Board                                              */
/* The onboard switches should be as follows: 1(off),2(off),3(on),4(on),5(off),6(off),7(off),8(off)  */
/*****************************************************************************************************/

#include <stdio.h>
#include <ArduinoJson.h>

// LCD Display
#include <Wire.h>
#include <LCD.h>
#include <LiquidCrystal_I2C.h>

// Real time clock
#include <DS1302.h>
#include <DateTimelib.h>

// Keypad includes
#include <Keypad.h>

// RFID includes
#include <SPI.h>
#include <MFRC522.h>

#include <avr/wdt.h>


#define EspSerial Serial3

// LCD Display definitions---------
const byte I2C_ADDR = 0x27; //Add your address here.  Find it from I2C Scanner
const byte BACKLIGHT_PIN = 3;
const byte En_pin = 2;
const byte Rw_pin = 1;
const byte Rs_pin = 0;
const byte D4_pin = 4;
const byte D5_pin = 5;
const byte D6_pin = 6;
const byte D7_pin = 7;
const byte led_pin = 13;

LiquidCrystal_I2C lcd(I2C_ADDR,En_pin,Rw_pin,Rs_pin,D4_pin,D5_pin,D6_pin,D7_pin);


byte WIFI_SYMBOL_CONNECTED[] = {
  B00000,
  B01110,
  B10001,
  B00100,
  B01010,
  B00000,
  B00100,
  B00000
};

byte WIFI_SYMBOL_DISCONNECTED[] = {
  B00001,
  B01111,
  B10011,
  B00100,
  B01110,
  B01000,
  B10100,
  B10000
};

byte ALARM_SYMBOL_ON[] = {
  B00000,
  B01110,
  B10001,
  B10001,
  B11111,
  B11011,
  B11011,
  B01110
/*  B00100,
  B01110,
  B01110,
  B01110,
  B11111,
  B00000,
  B00100,
  B00000
*/};

byte ALARM_SYMBOL_HALF_LOCKED[] = {
  B00000,
  B00000,
  B01110,
  B10001,
  B10001,
  B11111,
  B11011,
  B01110
};

byte ALARM_SYMBOL_OFF[] = {
  B01110,
  B10001,
  B10000,
  B10000,
  B11111,
  B11011,
  B11011,
  B01110
/*  B00101,
  B01111,
  B01110,
  B01110,
  B11111,
  B01000,
  B10100,
  B10000
*/};

const uint8_t WIFI_SYMBOL_CONNECTED_CHAR = 0;
const uint8_t WIFI_SYMBOL_DISCONNECTED_CHAR = 1;
const uint8_t ALARM_SYMBOL_ON_CHAR = 2;
const uint8_t ALARM_SYMBOL_HALF_LOCKED_CHAR = 3;
const uint8_t ALARM_SYMBOL_OFF_CHAR = 4;

bool WIFI_SYMBOL_ON = false;
bool TEXT_SHOW = false;
uint64_t START_BLINKING_TS;
bool IS_WIFI_CONNECTED;

unsigned char LCD_COLS = 16;
unsigned char LCD_ROWS = 2;

//int WIFI_CONNECTION_ATTEMPT = 0;
//char MAX_WIFI_CONNECTION_ATTEMPTS = 3;

// Keypad definitions-------
const byte ROWS = 4;
const byte COLS = 4;
char KEYPAD_KEYS[ROWS][COLS] = {
  {'1','2','3','A'},
  {'4','5','6','B'},
  {'7','8','9','C'},
  {'*','0','#','D'}
};

const byte ROW_PINS[ROWS] = {22, 24, 26, 28};
const byte COL_PINS[COLS] = {23, 25, 27, 29};

String PASSWORD_TYPED;
int TIMEOUT = 0;
unsigned long START_TIME = 0;
int SEC = 0;
int ELAPSED_SECONDS = 0;
const int SYSTEM_ALARM_ACTIVATION_TIMEOUT_SEC = 30; // The time in seconds to fully activate the alarm

// Init the DS1302
DS1302 rtc(40, 41, 42);

Keypad keypad = Keypad(makeKeymap(KEYPAD_KEYS), ROW_PINS, COL_PINS, ROWS, COLS);
//----------------------------

// RFID definitions
const byte SS_PIN = 53;
const byte RST_PIN = 49;
MFRC522 RFID_MFRC522(SS_PIN, RST_PIN);   // Create MFRC522 instance.


const byte BUZZER_PIN = 11;
const byte RED_PIN = 8;
const byte GREEN_PIN = 9;
const byte BLUE_PIN = 10;

typedef enum controller_state_ {
  CONTROLLER_INITIALIZED,
  CONTROLLER_VERIFICATION_STARTED,
  CONTROLLER_VERIFICATION_STOPPED,
  CONTROLLER_ACTIVATE_ALARM_MENU,
  CONTROLLER_DEACTIVATE_ALARM_MENU,
  CONTROLLER_FULLY_ACTIVATE_TIMEOUT
}controller_state;

typedef enum keypad_state_ {
  KEYPAD_INITIALIZED,
  KEYPAD_TYPING_STARTED,
  KEYPAD_TYPING_FINISHED
}kpad_state;

typedef enum verification_state_ {
  VERIFICATION_INITIALIZED,
  VERIFICATION_AUTHORIZED,
  VERIFICATION_UNAUTHORIZED
}verification_state;

typedef enum system_alarm_state_ {
  SYSTEM_ALARM_INITIALIZED,
  SYSTEM_ALARM_DISARMED,      // The alarm is deactivated
  SYSTEM_ALARM_FULLY_ARMED,   // The alarm is activated when no one in the house. Movement sensors active
  SYSTEM_ALARM_HALF_ARMED     // The alarm is activated during night when people in the house. Movement sensors inactive
}system_alarm_state;

const int VERIFY_TIMEOUT_SEC = 15;

String VERIFIED_USERS;
String VERIFIED_PASSWORDS;

controller_state CTRL_STATE;
kpad_state KEYPAD_STATE;
verification_state V_STATE;
system_alarm_state SYSTEM_ALARM_STATE;

void colorLed(unsigned char red, unsigned char green, unsigned char blue)
{
  analogWrite(RED_PIN, red);
  analogWrite(GREEN_PIN, green);
  analogWrite(BLUE_PIN, blue);
}

// Keypad functionality ----------------------------------------------------
bool validatePassword(String password_typed)
{
  StaticJsonBuffer<256> jsonBuffer;
  JsonArray& passwords = jsonBuffer.parseArray(VERIFIED_PASSWORDS);
  Serial.print("passwords.size(): ");
  Serial.println(passwords.size());
  for(int i=0; i < passwords.size(); ++i)
  {
    String password = passwords.get<String>(i);
    if(password.equals(password_typed))
      return true;
  }
  return false;
}

verification_state verifyKeypadPassword() {

  char key = keypad.getKey();
  if(key == '*' && KEYPAD_STATE != kpad_state::KEYPAD_TYPING_STARTED)
  {
    KEYPAD_STATE = kpad_state::KEYPAD_TYPING_STARTED;
    Serial.println("Password typing started...");
  }
  else if(key == '*' && KEYPAD_STATE == kpad_state::KEYPAD_TYPING_STARTED)
  {
    KEYPAD_STATE = KEYPAD_TYPING_FINISHED;
    Serial.println("Password typing finished");
  }

  if(key && key != '*' && KEYPAD_STATE == kpad_state::KEYPAD_TYPING_STARTED)
  {
    Serial.println(key);
    PASSWORD_TYPED += key;
    Serial.println(PASSWORD_TYPED);
  }

  if(KEYPAD_STATE == kpad_state::KEYPAD_TYPING_FINISHED)
  {
    if(validatePassword(PASSWORD_TYPED))
    {
      colorLed(0, 255, 0);
      lcdClearLine(1);
      displayMessageAt(2, 1, "Password OK!");
      buzzCorrectPassword();
      V_STATE = verification_state::VERIFICATION_AUTHORIZED;
    } else {
      colorLed(255, 0, 0);
      lcdClearLine(1);
      displayMessageAt(1, 1, "Wrong password!");
      buzzWrongPassword();
      colorLed(0, 0, 0);
      V_STATE = verification_state::VERIFICATION_UNAUTHORIZED;
    }
    PASSWORD_TYPED = "";
    KEYPAD_STATE = kpad_state::KEYPAD_INITIALIZED;
  }
  return V_STATE;
}

void buzzCorrectPassword() {
  for(int i=0;i<85;i++)
  {
    digitalWrite(BUZZER_PIN,HIGH);
    delay(2);//wait for 1ms
    digitalWrite(BUZZER_PIN,LOW);
    delay(2);//wait for 1ms
  }
  //output another frequency
  for(int i=0;i<90;i++)
  {
    digitalWrite(BUZZER_PIN,HIGH);
    delay(1);//wait for 2ms
    digitalWrite(BUZZER_PIN,LOW);
    delay(1);//wait for 2ms
  }
  digitalWrite(BUZZER_PIN,HIGH);
}

void buzzWrongPassword() {
  for(int i=0;i<40;i++)
  {
    digitalWrite(BUZZER_PIN,HIGH);
    delay(4);//wait for 1ms
    digitalWrite(BUZZER_PIN,LOW);
    delay(4);//wait for 1ms
  }
  digitalWrite(BUZZER_PIN,HIGH);
}
// End of keypad functionality ---------------------------------------------

// RFID functionality ------------------------------------------------------
bool validateUserId(String id)
{
  StaticJsonBuffer<256> jsonBuffer;
  JsonArray& ids = jsonBuffer.parseArray(VERIFIED_USERS);
  for(int i=0; i < ids.size(); ++i)
  {
    String valid_id = ids.get<String>(i);
    Serial.print("valid_id: ");
    Serial.println(valid_id);
    Serial.print("id: ");
    Serial.println(id);
    if(valid_id.equals(id))
      return true;
  }
  return false;
}

verification_state verifyRfidCard()
{
  // Look for new cards
  if ( ! RFID_MFRC522.PICC_IsNewCardPresent()) 
  {
    //Serial.println("No Card present");
    V_STATE = verification_state::VERIFICATION_INITIALIZED;
    return V_STATE;
  }
  // Select one of the cards
  if ( ! RFID_MFRC522.PICC_ReadCardSerial()) 
  {
    //Serial.println("Failed reading card");
    V_STATE = verification_state::VERIFICATION_INITIALIZED;
    return V_STATE;
  }

  //Show UID on serial monitor
  Serial.println();
  Serial.print(" UID tag :");
  String content= "";
  byte letter;
  for (byte i = 0; i < RFID_MFRC522.uid.size; i++) 
  {
     Serial.print(RFID_MFRC522.uid.uidByte[i] < 0x10 ? " 0" : " ");
     Serial.print(RFID_MFRC522.uid.uidByte[i], HEX);
     content.concat(String(RFID_MFRC522.uid.uidByte[i] < 0x10 ? " 0" : " "));
     content.concat(String(RFID_MFRC522.uid.uidByte[i], HEX));
  }
  Serial.println();
  content.toUpperCase();

  if (validateUserId(content.substring(1))) //change UID of the card that you want to give access
  {
    colorLed(0, 255, 0);
    lcdClearLine(1);
    displayMessageAt(1, 1, "Welcome Christo!");
    buzzCorrectPassword();
    delay(2000);
    V_STATE = verification_state::VERIFICATION_AUTHORIZED;
    return V_STATE;
  }
  else
  {
    colorLed(255, 0, 0);
    lcdClearLine(1);
    displayMessageAt(1, 1, " Access Denied ");
    buzzWrongPassword();
    V_STATE = verification_state::VERIFICATION_UNAUTHORIZED;
    return V_STATE;
  }
}
//--------------------------------------------------------------------------

void sendMessageToEspSerial(char* message)
{
  Serial3.println(message);
}

void initVerifyPersonResource()
{

  CTRL_STATE = controller_state::CONTROLLER_INITIALIZED;
  PASSWORD_TYPED = "";
  KEYPAD_STATE = KEYPAD_INITIALIZED;
}

void displayMessageAt(byte col, byte row, char* message)
{
  lcd.setCursor(col, row);
  lcd.write(message);
}

void lcdClearLine(int line)
{
  lcd.setCursor(0, line);
  for(int i = 0; i <= LCD_COLS; ++i)
    lcd.write(" ");
}

void verificationProcess()
{
  if(ELAPSED_SECONDS <= TIMEOUT)
  {
    if(verifyRfidCard() == verification_state::VERIFICATION_AUTHORIZED)
    {
      CTRL_STATE = controller_state::CONTROLLER_VERIFICATION_STOPPED;
      sendMessageToEspSerial("@V1");
      lcdClearLine(1);
      delay(3000);
      //lcd.setBacklight(LOW);
      return;
    }
    if(verifyKeypadPassword() == verification_state::VERIFICATION_AUTHORIZED)
    {
      CTRL_STATE = controller_state::CONTROLLER_VERIFICATION_STOPPED;
      sendMessageToEspSerial("@V1");
      lcdClearLine(1);
      delay(3000);
      //lcd.setBacklight(LOW);
      return;
    }
    if(millis() > START_TIME + (ELAPSED_SECONDS * 1000))
    {
      char str_sec[3] = {0,0,0};
      sprintf(str_sec, "Alarm in %7d", SEC);
      displayMessageAt(0, 1, str_sec);
      Serial.print(str_sec);
      Serial.println(" seconds");
      ELAPSED_SECONDS++;
      SEC--;
      colorLed(237, 109, 0);
    }
  }
  else
  {
    V_STATE = verification_state::VERIFICATION_UNAUTHORIZED;
    PASSWORD_TYPED = "";
    lcdClearLine(1);
    displayMessageAt(0, 1, " UNAUTHORIZED! ");
    colorLed(255, 0, 0);
    sendMessageToEspSerial("@V0");
    delay(3000);
    lcdClearLine(1);
    displayMessageAt(0, 1, "    ALARM!!! ");
    CTRL_STATE = controller_state::CONTROLLER_VERIFICATION_STOPPED;
  }
}

void blinkSymbol(int col, int row, uint8_t symbol)
{
  if(millis() < START_BLINKING_TS + 500)
    return;

  START_BLINKING_TS = millis();
  lcd.setCursor(col, row);
  if(WIFI_SYMBOL_ON)
  {
    colorLed(0, 0, 0);
    WIFI_SYMBOL_ON = false;
    lcd.write(" ");
  }
  else
  {
    colorLed(0, 0, 255);
    WIFI_SYMBOL_ON = true;
    lcd.write(symbol);
  }
}

void blinkText(int col, int row, char* text)
{
  if(millis() < START_BLINKING_TS + 500)
    return;

  START_BLINKING_TS = millis();
  lcd.setCursor(col, row);
  if(TEXT_SHOW)
  {
    colorLed(0, 0, 0);
    TEXT_SHOW = false;
    lcdClearLine(row);
  }
  else
  {
    colorLed(255, 0, 0);
    TEXT_SHOW = true;
    displayMessageAt(col, row, text);
    buzzWrongPassword();
  }
}

void lcdShowWifiSymbol()
{
  lcd.setCursor(1, 0);
  if(IS_WIFI_CONNECTED)
  {
    colorLed(0, 0, 255);
    lcd.write(WIFI_SYMBOL_CONNECTED_CHAR);
  }
  else if(START_BLINKING_TS != 0)
  {
    blinkSymbol(1, 0, WIFI_SYMBOL_CONNECTED_CHAR);
  }
}

void lcdShowAlarmSymbol()
{
  lcd.setCursor(0, 0);
  switch(SYSTEM_ALARM_STATE)
  {
    case system_alarm_state::SYSTEM_ALARM_DISARMED:
      lcd.write(ALARM_SYMBOL_OFF_CHAR);
      break;
    case system_alarm_state::SYSTEM_ALARM_HALF_ARMED:
      lcd.write(ALARM_SYMBOL_HALF_LOCKED_CHAR);
      break;
    case system_alarm_state::SYSTEM_ALARM_FULLY_ARMED:
      lcd.write(ALARM_SYMBOL_ON_CHAR);
      break;
    default:
      break;
  }
}

void lcdShowCurrentTime()
{
  lcd.setCursor(8, 0);
  lcd.write(rtc.getTimeStr());
}

void softwareReset( uint8_t prescaller) {
  // start watchdog with the provided prescaller
  wdt_enable( prescaller);
  // wait for the prescaller time to expire
  // without sending the reset signal by using
  // the wdt_reset() method
  while(1) {}
}

void showMenu()
{
  switch(CTRL_STATE)
  {
    case controller_state::CONTROLLER_DEACTIVATE_ALARM_MENU:
      displayMessageAt(0, 0, "ALARM OFF       ");
      displayMessageAt(0, 1, "A=YES, B=NO    ");
      break;
    case controller_state::CONTROLLER_ACTIVATE_ALARM_MENU:
      displayMessageAt(0, 0, "ALARM ON        ");
      displayMessageAt(0, 1, "A=FULLY, B=HALF ");
      break;
  }
  checkForKeypadPressed();
}

void checkForKeypadPressed()
{
  char key = keypad.getKey();
  
  if(key)
  {
    Serial.println(key);
  }
  if(CTRL_STATE != CONTROLLER_DEACTIVATE_ALARM_MENU && 
      CTRL_STATE != CONTROLLER_ACTIVATE_ALARM_MENU)
  {
    switch(SYSTEM_ALARM_STATE)
    {
      case system_alarm_state::SYSTEM_ALARM_HALF_ARMED:
        if(key == '#')
        {
          CTRL_STATE = controller_state::CONTROLLER_DEACTIVATE_ALARM_MENU;
          Serial.println("Showing deactivate alarm menu...");
        }
        break;
      case system_alarm_state::SYSTEM_ALARM_DISARMED:
        if(key == '#')
        {
          CTRL_STATE = controller_state::CONTROLLER_ACTIVATE_ALARM_MENU;
          Serial.println("Show activate alarm menu...");
        }
        break;
    }
  }

  switch(CTRL_STATE)
  {
    case controller_state::CONTROLLER_DEACTIVATE_ALARM_MENU:
      if(key == 'A')
      {
        SYSTEM_ALARM_STATE = system_alarm_state::SYSTEM_ALARM_DISARMED;
        sendMessageToEspSerial("@A0");
        CTRL_STATE = controller_state::CONTROLLER_INITIALIZED;
        lcdClearLine(0);
        lcdClearLine(1);
      }
      else if(key == 'B')
      {
        CTRL_STATE = controller_state::CONTROLLER_INITIALIZED;
        lcdClearLine(0);
        lcdClearLine(1);
      }
      break;
    case controller_state::CONTROLLER_ACTIVATE_ALARM_MENU:
      if(key == 'A')
      {
        TIMEOUT = SYSTEM_ALARM_ACTIVATION_TIMEOUT_SEC;
        START_TIME = millis();
        SEC = TIMEOUT;
        ELAPSED_SECONDS = 0;
        CTRL_STATE = controller_state::CONTROLLER_FULLY_ACTIVATE_TIMEOUT;
        lcdClearLine(0);
        lcdClearLine(1);
      }
      else if(key == 'B')
      {
        SYSTEM_ALARM_STATE = system_alarm_state::SYSTEM_ALARM_HALF_ARMED;
        sendMessageToEspSerial("@A2");
        CTRL_STATE = controller_state::CONTROLLER_INITIALIZED;
        lcdClearLine(0);
        lcdClearLine(1);
      }
      break;
  }
}

void activateAlarmInTimeout()
{
  if(ELAPSED_SECONDS <= TIMEOUT && CTRL_STATE == controller_state::CONTROLLER_FULLY_ACTIVATE_TIMEOUT)
  {
    if(millis() > START_TIME + (ELAPSED_SECONDS * 1000))
    {
      char str_sec[3] = {0,0,0};
      sprintf(str_sec, "Activate in %4d", SEC);
      displayMessageAt(0, 1, str_sec);
      Serial.print(str_sec);
      Serial.println(" seconds");
      ELAPSED_SECONDS++;
      SEC--;
    }
  }
  else
  {
    SYSTEM_ALARM_STATE = system_alarm_state::SYSTEM_ALARM_FULLY_ARMED;
    sendMessageToEspSerial("@A1");
    CTRL_STATE = controller_state::CONTROLLER_INITIALIZED;
    lcdClearLine(0);
    lcdClearLine(1);
  }
}

void checkForEspMessage()
{
  String esp_message;
  char data = 0;
  while(Serial3.available() && data != 10)
  {
    data=Serial3.read();
    esp_message += data;
  }
  if(esp_message.length() > 0)
  {
     Serial.print("esp_message: ");
     Serial.println(esp_message);
     if(esp_message.startsWith("@"))
       handleEspMessage(esp_message);
  }
}

void handleEspMessage(String message)
{
  if(message.startsWith("@C")) // Connection
  {
    colorLed(0, 0, 255);
    IS_WIFI_CONNECTED = message.charAt(2) == '1' ? true : false;
    if(IS_WIFI_CONNECTED)
    {
      lcdShowWifiSymbol();
      sendMessageToEspSerial("@A");
    }
    else
    {
      colorLed(255, 0, 0);
      lcd.write(WIFI_SYMBOL_DISCONNECTED_CHAR);
    }
    message = "";
  }
  if(message.startsWith("@V")) // Verification
  {
    if(SYSTEM_ALARM_STATE == system_alarm_state::SYSTEM_ALARM_DISARMED)
      return;
    colorLed(0, 255, 0);
    String str_timeout_sec = message.substring(2, message.length() - 2);
    message = "";
    initVerifyPersonResource();
    lcd.setBacklight(HIGH);
    TIMEOUT = str_timeout_sec.toInt();
    START_TIME = millis();
    SEC = TIMEOUT;
    ELAPSED_SECONDS = 0;
    CTRL_STATE = controller_state::CONTROLLER_VERIFICATION_STARTED;
  }
  if(message.startsWith("@A")) // Alarm status
  {
    colorLed(0, 255, 0);
    Serial.print("Alarm status response: ");
    Serial.println(message);
    char alarm_state = message.charAt(2);
    switch(alarm_state)
    {
      case '0':
        SYSTEM_ALARM_STATE = system_alarm_state::SYSTEM_ALARM_DISARMED;
        break;
      case '1':
        SYSTEM_ALARM_STATE = system_alarm_state::SYSTEM_ALARM_FULLY_ARMED;
        break;
      case '2':
        SYSTEM_ALARM_STATE = system_alarm_state::SYSTEM_ALARM_HALF_ARMED;
        break;
      default:
        SYSTEM_ALARM_STATE = system_alarm_state::SYSTEM_ALARM_INITIALIZED;
        break;
    }
    delay(1000);
    //lcd.setBacklight(LOW);
    message = "";
  }
  if(message.startsWith("@U"))
  {
    VERIFIED_USERS = message.substring(2, message.length() - 2);;
    Serial.print("Verified users list: ");
    Serial.println(VERIFIED_USERS);
  }
  if(message.startsWith("@P"))
  {
    VERIFIED_PASSWORDS = message.substring(2, message.length() - 2);;
    Serial.print("Verified passwords list: ");
    Serial.println(VERIFIED_PASSWORDS);
  }
  if(message.startsWith("@L")) // Alarm status
  {
    Serial.print("Stop alarm received: ");
    Serial.println(message);
    if(message.charAt(2) == '0')
    {
      lcdClearLine(1);
      V_STATE = verification_state::VERIFICATION_AUTHORIZED;
    }
    message = "";
  }
  if(message.startsWith("@R")) // Esp is restarting
  {
    Serial.print("Esp is restarting: ");
    Serial.println(message);
//    WIFI_CONNECTION_ATTEMPT++;
//    if(WIFI_CONNECTION_ATTEMPT >= MAX_WIFI_CONNECTION_ATTEMPTS)
//      softwareReset( WDTO_60MS);
    message = "";
  }
}

void setup(void)
{
  Serial.begin(115200);
  Serial3.begin(115200);

  lcd.begin (16,2); //My LCD was 16x2
  lcd.setBacklightPin(BACKLIGHT_PIN,POSITIVE);
  lcd.setBacklight(HIGH);
  lcd.createChar(WIFI_SYMBOL_CONNECTED_CHAR, WIFI_SYMBOL_CONNECTED);
  lcd.createChar(WIFI_SYMBOL_DISCONNECTED_CHAR, WIFI_SYMBOL_DISCONNECTED);
  lcd.createChar(ALARM_SYMBOL_ON_CHAR, ALARM_SYMBOL_ON);
  lcd.createChar(ALARM_SYMBOL_HALF_LOCKED_CHAR, ALARM_SYMBOL_HALF_LOCKED);
  lcd.createChar(ALARM_SYMBOL_OFF_CHAR, ALARM_SYMBOL_OFF);
  lcd.home (); // go home

  SPI.begin();      // Initiate  SPI bus
  RFID_MFRC522.PCD_Init();   // Initiate RFID_MFRC522

  rtc.halt(false);
  rtc.writeProtect(true);
/*  DateTime dt(__DATE__, __TIME__);
  rtc.setDOW(dt.dayOfWeek());
  rtc.setTime(dt.hour(), dt.minute(), dt.second());
  rtc.setDate(dt.day(), dt.month(), dt.year());
  Serial.print("Setting date: ");
  Serial.println(__DATE__);
  Serial.print("Setting time: ");
  Serial.println(__TIME__);
*/
  initVerifyPersonResource();
  pinMode(BUZZER_PIN, OUTPUT);
  pinMode(RED_PIN, OUTPUT);
  pinMode(GREEN_PIN, OUTPUT);
  pinMode(BLUE_PIN, OUTPUT);
  digitalWrite(BUZZER_PIN, HIGH);
  colorLed(0, 0, 0);
  KEYPAD_STATE = KEYPAD_INITIALIZED;
  IS_WIFI_CONNECTED = false;
  START_BLINKING_TS = millis();
  CTRL_STATE = controller_state::CONTROLLER_INITIALIZED;
  V_STATE = verification_state::VERIFICATION_INITIALIZED;
  SYSTEM_ALARM_STATE = system_alarm_state::SYSTEM_ALARM_INITIALIZED;
}

void loop(void)
{
  delay(10);

  checkForEspMessage();

  switch(CTRL_STATE)
  {
    case controller_state::CONTROLLER_VERIFICATION_STARTED:
      lcdShowAlarmSymbol();
      lcdShowWifiSymbol();
      lcdShowCurrentTime();
      verificationProcess();
      break;
    case controller_state::CONTROLLER_VERIFICATION_STOPPED:
    case controller_state::CONTROLLER_INITIALIZED:
      lcdShowAlarmSymbol();
      lcdShowWifiSymbol();
      lcdShowCurrentTime();
      if(SYSTEM_ALARM_STATE != system_alarm_state::SYSTEM_ALARM_FULLY_ARMED)
        checkForKeypadPressed();
      break;
    case controller_state::CONTROLLER_DEACTIVATE_ALARM_MENU:
    case controller_state::CONTROLLER_ACTIVATE_ALARM_MENU:
      if(SYSTEM_ALARM_STATE != system_alarm_state::SYSTEM_ALARM_FULLY_ARMED)
        showMenu();
      break;
    case controller_state::CONTROLLER_FULLY_ACTIVATE_TIMEOUT:
      lcdShowAlarmSymbol();
      lcdShowWifiSymbol();
      lcdShowCurrentTime();
      activateAlarmInTimeout();
      break;
    default:
      lcdShowAlarmSymbol();
      lcdShowWifiSymbol();
      lcdShowCurrentTime();
  }

  switch(V_STATE)
  {
    case verification_state::VERIFICATION_UNAUTHORIZED:
      blinkText(0, 1, "    ALARM!!! ");
      break;
  }

}
