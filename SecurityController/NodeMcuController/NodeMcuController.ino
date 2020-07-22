#include <stdio.h>
#include <ESP8266WebServer.h>
#include <ArduinoJson.h>
#include "C:\Development\Home_Projects\Arduino\Home_Security\Common\BatteryVoltage.h"
#include "C:\Development\Home_Projects\Arduino\Home_Security\Common\ESP8266\HttpClientCommands.h"
#include "C:\Development\Home_Projects\Arduino\Home_Security\Common\WifiConfiguration.h"
#include "C:\Development\Home_Projects\Arduino\Home_Security\Common\HomeServerSettings.h"
#include "C:\Development\Home_Projects\Arduino\Home_Security\Common\WifiCredentials.h"

#include <SPI.h>
#include <MFRC522.h>

#include <EEPROM.h>

//#define _DEBUG

//REST server settings
const int REST_SERVER_PORT = 8080;

//Rest Server Resources
const char* RES_PERSON_VERIFIED = "/verify_person";
const char* RES_VERIFIED_PASSWORDS = "/verified_passwords";
const char* RES_REGISTERED_PERSONS = "/persons";
const char* RES_VERIFY_PERSON_BY_PASSWORD = "/verify_person_by_password";
const char* RES_VERIFY_PERSON_BY_RFID_CARD = "/verify_person_by_rfid_card";
const char* RES_ALARM = "/alarm";
const char* RES_ALARM_STATE = "/alarm_state";
const char* RES_ALARM_ID = "/1";

int ALARM_STATE = -1;
int TEMP_ALARM_STATE = -1;

bool ALARM_STATE_UPDATE_TIMEOUT_INITIATED = false;
bool ALARM_STATE_UPDATED = false;
int ALARM_STATE_UPDATE_TIMEOUT_SEC = 2; // Wait time for the alarm state to get updated in the controller
unsigned long START_TIME = 0;
int ELAPSED_SECONDS = 0;

/*int EEPROM_ADDR = 0;
int CONNECTION_ATTEMPT = 1;
char MAX_CONNECTION_ATTEMPTS = 3;
*/
ESP8266WebServer HTTP_REST_SERVER(REST_SERVER_PORT);

void DEBUG_PRINT(const char* msg, bool new_line)
{
  #ifdef _DEBUG
  if(new_line)
    Serial.println(msg);
  else
    Serial.print(msg);
  #endif
}

int initWifi() {
  int retries = 0;

  sendMessageToMega2560Serial("Connecting to WiFi ..........");

  WiFi.mode(WIFI_STA);
  WiFi.begin(WIFI_SSID, WIFI_PASSWD);
  // check the status of WiFi connection to be WL_CONNECTED
  while ((WiFi.status() != WL_CONNECTED) && (retries < MAX_WIFI_INIT_RETRY)) {
      retries++;
      delay(WIFI_RETRY_DELAY_MS);
  }

  return WiFi.status(); // return the WiFi connection status
}

void json_to_resource(JsonObject& jsonBody) {
  ALARM_STATE = jsonBody["alarm_state"];
}

void postAlarmState(int alarm_state)
{
  StaticJsonBuffer<200> jsonBuffer;
  JsonObject& jsonObj = jsonBuffer.createObject();
  jsonObj["alarm_id"] = 1;
  jsonObj["alarm_on"] = false;
  jsonObj["alarm_state"] = alarm_state;
  String body;
  jsonObj.printTo(body);
  String payload;
  sendMessageToMega2560Serial(body.c_str());
  int statusCode = httpPost(HOME_SERVER_ADDRESS, HOME_SERVER_REST_PORT, RES_ALARM, body.c_str(), payload);
  ALARM_STATE = alarm_state;
}
/*
void postPersonVerified(bool verified)
{
  StaticJsonBuffer<200> jsonBuffer;
  JsonObject& jsonObj = jsonBuffer.createObject();
  jsonObj["persons"] = verified;
  String body;
  jsonObj.printTo(body);
  String payload;
  int statusCode = httpPost(HOME_SERVER_ADDRESS, HOME_SERVER_REST_PORT, RES_PERSON_VERIFIED, body.c_str(), payload);
}
*/
int getAlarmState()
{
  int statusCode = 0;
  String alarmState;
  String res_alarm_endpoint = RES_ALARM_STATE;
  res_alarm_endpoint += RES_ALARM_ID;
  statusCode = httpGet(HOME_SERVER_ADDRESS, HOME_SERVER_REST_PORT, res_alarm_endpoint.c_str(), alarmState);
  sendMessageToMega2560Serial("Got alarm state returned status: ");
  sendMessageToMega2560Serial(alarmState.c_str());
  sendMessageToMega2560Serial("Got alarm state: ");
  sendMessageToMega2560Serial(String(statusCode).c_str());
  return alarmState.toInt();
}

bool verifyPersonByPassword(String password)
{
  StaticJsonBuffer<200> jsonBuffer;
  JsonObject& jsonObj = jsonBuffer.createObject();
  jsonObj["password"] = password;
  String body;
  jsonObj.printTo(body);
  String payload;
  int statusCode = httpPost(HOME_SERVER_ADDRESS, HOME_SERVER_REST_PORT, RES_VERIFY_PERSON_BY_PASSWORD, body.c_str(), payload);
  return statusCode == 200;
}

bool verifyPersonByRfidCard(String rfidCardId)
{
  StaticJsonBuffer<200> jsonBuffer;
  JsonObject& jsonObj = jsonBuffer.createObject();
  jsonObj["rfid_card_id"] = rfidCardId;
  String body;
  jsonObj.printTo(body);
  String payload;
  int statusCode = httpPost(HOME_SERVER_ADDRESS, HOME_SERVER_REST_PORT, RES_VERIFY_PERSON_BY_RFID_CARD, body.c_str(), payload);
  return statusCode == 200;
}

void onInitiateVerificationProcess()
{
/*  Serial.print("HTTP Method: ");
  Serial.println(HTTP_REST_SERVER.method());
*/  
  if (HTTP_REST_SERVER.method() == HTTP_GET)
  {
    sendMessageToMega2560Serial("@V30");
    String registered_rfid_card_ids, registered_passwords;
//    getRegisteredPersons(registered_rfid_card_ids, registered_passwords);
//    registered_rfid_card_ids = "@U" + registered_rfid_card_ids;
//    sendMessageToMega2560Serial(registered_rfid_card_ids.c_str());
//    registered_passwords = "@P" + registered_passwords;
//    sendMessageToMega2560Serial(registered_passwords.c_str());
    HTTP_REST_SERVER.sendHeader("Location", "/initiate_verification_process");
    HTTP_REST_SERVER.send(200);
  }
  else
  {
    HTTP_REST_SERVER.send(404);
  }
}

void onStopAlarm()
{
/*  Serial.print("HTTP Method: ");
  Serial.println(HTTP_REST_SERVER.method());
*/  
  if (HTTP_REST_SERVER.method() == HTTP_GET)
  {
    sendMessageToMega2560Serial("@L0");
    HTTP_REST_SERVER.sendHeader("Location", "/stop_alarm");
    HTTP_REST_SERVER.send(200);
  }
  else
  {
    HTTP_REST_SERVER.send(404);
  }
}

void onPostSystemAlarm()
{
  if (HTTP_REST_SERVER.method() == HTTP_POST)
  {
    if (HTTP_REST_SERVER.hasArg("plain")== false){ //Check if body received
      Serial.println("Payload is empty");
      HTTP_REST_SERVER.send(400, "text/plain", "Payload is empty");
      return;
    }
    String payload;
    payload = HTTP_REST_SERVER.arg("plain");
/*    Serial.println("Payload: ");
    Serial.println(payload.c_str());
*/    StaticJsonBuffer<200> jsonBuffer;
    JsonObject& jsonBody = jsonBuffer.parseObject(payload);
    int alarm_state = jsonBody["system_alarm_state"];
    String msg_system_alarm_state = "@A";
    msg_system_alarm_state += String(alarm_state);
/*    Serial.println("Sending system alarm state: ");
    Serial.println(msg_system_alarm_state.c_str());
*/  sendMessageToMega2560Serial(msg_system_alarm_state.c_str());
  }
  else
  {
    HTTP_REST_SERVER.send(404);
  }
}

void espRestart()
{
  ESP.restart();
}

void configRestServerRouting() {
    HTTP_REST_SERVER.on("/", HTTP_GET, []() {
        HTTP_REST_SERVER.send(200, "text/html",
            "Hello from Security controller REST Web Server");
    });

    HTTP_REST_SERVER.on("/esp_restart", HTTP_GET, []() {
      HTTP_REST_SERVER.send(200, "text/html", "Security controller is restarting...");
      espRestart();
    });

    HTTP_REST_SERVER.on("/initiate_verification_process", HTTP_GET, []() {
      HTTP_REST_SERVER.send(200, "text/html", "Verification process initiated");
      onInitiateVerificationProcess();
    });

    HTTP_REST_SERVER.on("/stop_alarm", HTTP_GET, []() {
      HTTP_REST_SERVER.send(200, "text/html", "The alarm is forced to stop");
      onStopAlarm();
    });

    HTTP_REST_SERVER.on("/alarm_state", HTTP_POST, []() {
      HTTP_REST_SERVER.send(200, "text/html", "Got system alarm state");
      onPostSystemAlarm();
    });
}

void waitForAlarmStateUpdate()
{
  if (ELAPSED_SECONDS <= ALARM_STATE_UPDATE_TIMEOUT_SEC)
  {
    if (millis() > START_TIME + (ELAPSED_SECONDS * 1000))
    {
      ELAPSED_SECONDS++;
    }
  }
  else
  {
    StaticJsonBuffer<200> jsonBuffer;
    JsonObject& jsonObj = jsonBuffer.createObject();
    sendMessageToMega2560Serial("The new alarm state could not get updated to the security controller");
    ALARM_STATE_UPDATE_TIMEOUT_INITIATED = false;
    if(!ALARM_STATE_UPDATED)
      initiateGetAlarmState();
  }
}

void initiateGetAlarmState()
{
  TEMP_ALARM_STATE = getAlarmState();
  String str_alarm_state = "@A";
  str_alarm_state += String(TEMP_ALARM_STATE);
  delay(30);
  sendMessageToMega2560Serial(str_alarm_state.c_str());
  // We start a timer of ALARM_STATE_UPDATE_TIMEOUT_SEC seconds
  // in order to get a @AOK from the security controller when the alarm state is update. If it didn't then we will retry.
  START_TIME = millis();
  ELAPSED_SECONDS = 0;
  ALARM_STATE_UPDATE_TIMEOUT_INITIATED = true;
  ALARM_STATE_UPDATED = false;
}

void handleMega2560Message(String message)
{
  if(message.startsWith("@A"))
  {
    switch(message.charAt(2))
    {
      case '?': 
      {
        if(ALARM_STATE_UPDATE_TIMEOUT_INITIATED) // If we are in process of getting the alarm state, exit
        {
          sendMessageToMega2560Serial("Getting the alarm state is in progress");
          return;
        }
        initiateGetAlarmState();
      }
      break;
      case 'O': // This is when the security cotroller has updated the alarm state and sends back '@AOK'
      {
        sendMessageToMega2560Serial("In message == @AOK");
        HTTP_REST_SERVER.sendHeader("Location", "/alarm_state");
        HTTP_REST_SERVER.send(200);
        ALARM_STATE_UPDATE_TIMEOUT_INITIATED = false;
        ALARM_STATE = TEMP_ALARM_STATE;
        ALARM_STATE_UPDATED = true;
      }
      break;
      case '0':
      case '1':
      case '2':
      {
        message = message.substring(2);
        int alarm_state = message.toInt();
        sendMessageToMega2560Serial("Will post alarm state. message: ");
        sendMessageToMega2560Serial(message.c_str());
  
        postAlarmState(alarm_state);
      }
      break;
    }
  }
  else if(message.startsWith("@P")) // Got password from security controller
  {
    String password = message.substring(2, message.length() - 1);
    sendMessageToMega2560Serial("Got password : ");
    sendMessageToMega2560Serial(password.c_str());
    int ind = password.indexOf('#');
    if(ind != -1)
    {
      password = password.substring(0, ind);
      bool verified = verifyPersonByPassword(password);
      String passwordVerified = "@P";
      passwordVerified += (verified ? "1" : "0");
      sendMessageToMega2560Serial(passwordVerified.c_str());
    }
    else
    {
      String passwordVerified = "@P0";
      sendMessageToMega2560Serial(passwordVerified.c_str());
    }
  }
  else if(message.startsWith("@I")) // Got password from security controller
  {
    String rfidCardId = message.substring(2, message.length() - 1);
    sendMessageToMega2560Serial("Got password : ");
    sendMessageToMega2560Serial(rfidCardId.c_str());
    int ind = rfidCardId.indexOf('#');
    if(ind != -1)
    {
      rfidCardId = rfidCardId.substring(0, ind);
      bool verified = verifyPersonByRfidCard(rfidCardId);
      String rfidCardVerified = "@I";
      rfidCardVerified += (verified ? "1" : "0");
      sendMessageToMega2560Serial(rfidCardVerified.c_str());
    }
    else
    {
      String rfidCardVerified = "@I0";
      sendMessageToMega2560Serial(rfidCardVerified.c_str());
    }
  }
/*  
  else if(message.startsWith("@V"))
  {
    if(message[2] == '1')
      postPersonVerified(true);
    else
      postPersonVerified(false);
  }
*/
}

void sendMessageToMega2560Serial(const char* message)
{
  Serial.println(message);
}

void setup() {
  Serial.begin(115200);
  if (initWifi() == WL_CONNECTED) {
    sendMessageToMega2560Serial("@C1");

    configRestServerRouting();
    HTTP_REST_SERVER.begin();
//  Serial.println("HTTP REST Server Started");
  /*    Serial.print("Connected to ");
      Serial.print(WIFI_SSID);
      Serial.print("--- IP: ");
      Serial.println(WiFi.localIP());
  */
  }
  else {
  /*    Serial.print("Error connecting to: ");
      Serial.println(WIFI_SSID);
      Serial.println("Restarting...");
  */
    sendMessageToMega2560Serial("@R");
    delay(1000);
    ESP.restart();
  }
//  }
}

void loop() {
  delay(100);
  HTTP_REST_SERVER.handleClient();
  String mega2560Message = "";
  while(Serial.available())
  {
    char data;
    data=Serial.read();
    mega2560Message +=data;
/*    Serial.print("data: ");
    Serial.println(data);
    Serial.print("esp_message: ");
    Serial.println(mega2560Message);
*/
  }
  if(mega2560Message.length())
    handleMega2560Message(mega2560Message);
  if(ALARM_STATE_UPDATE_TIMEOUT_INITIATED)
    waitForAlarmStateUpdate();
}
