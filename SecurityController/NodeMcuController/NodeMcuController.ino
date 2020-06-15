#include <stdio.h>
#include <ESP8266WebServer.h>
#include <ArduinoJson.h>
#include "C:\Development\Home_Projects\Arduino\Home_Security\Common\HttpClientCommands.h"
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
const char* RES_VERIFIED_USERS = "/verified_users";
const char* RES_VERIFIED_PASSWORDS = "/verified_passwords";
const char* RES_ALARM_STATE = "/alarm_state";

int ALARM_STATE = 0;

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
  jsonObj["alarm_state"] = alarm_state;
  String body;
  jsonObj.printTo(body);
  String payload;
  int statusCode = httpPost(HOME_SERVER_ADDRESS, String(HOME_SERVER_REST_PORT), RES_ALARM_STATE, body, payload);
}

void postPersonVerified(bool verified)
{
  StaticJsonBuffer<200> jsonBuffer;
  JsonObject& jsonObj = jsonBuffer.createObject();
  jsonObj["personVerified"] = verified;
  String body;
  jsonObj.printTo(body);
  String payload;
  int statusCode = httpPost(HOME_SERVER_ADDRESS, String(HOME_SERVER_REST_PORT), RES_PERSON_VERIFIED, body, payload);
}

int getAlarmState()
{
  int statusCode = 0;
  int alarm_state;
  String payload;
  statusCode = httpGet(HOME_SERVER_ADDRESS, String(HOME_SERVER_REST_PORT), RES_ALARM_STATE, payload);
  StaticJsonBuffer<200> jsonBuffer;
  JsonObject& jsonBody = jsonBuffer.parseObject(payload);
  alarm_state = jsonBody["alarm_state"];
  return alarm_state;
}

String getVerifiedUsers()
{
  int statusCode = 0;
  String payload;
  statusCode = httpGet(HOME_SERVER_ADDRESS, String(HOME_SERVER_REST_PORT), RES_VERIFIED_USERS, payload);
  StaticJsonBuffer<256> jsonBuffer;
  JsonObject& obj_ids = jsonBuffer.parseObject(payload);
  String verified_users = obj_ids["ids"];
  return verified_users;
}

String getVerifiedPasswords()
{
  int statusCode = 0;
  String payload;
  statusCode = httpGet(HOME_SERVER_ADDRESS, String(HOME_SERVER_REST_PORT), RES_VERIFIED_PASSWORDS, payload);
  StaticJsonBuffer<256> jsonBuffer;
  JsonObject& obj_ids = jsonBuffer.parseObject(payload);
  String verified_passwords = obj_ids["passwords"];
  return verified_passwords;
}

void onInitiateVerificationProcess()
{
/*  Serial.print("HTTP Method: ");
  Serial.println(HTTP_REST_SERVER.method());
*/  
  if (HTTP_REST_SERVER.method() == HTTP_GET)
  {
    sendMessageToMega2560Serial("@V30");
    String verified_users = "@U" + getVerifiedUsers();
    sendMessageToMega2560Serial(verified_users.c_str());
    String verified_passwords = "@P" + getVerifiedPasswords();
    sendMessageToMega2560Serial(verified_passwords.c_str());
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
*/    sendMessageToMega2560Serial(msg_system_alarm_state.c_str());
    HTTP_REST_SERVER.sendHeader("Location", "/system_alarm_state");
    HTTP_REST_SERVER.send(200);
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

    HTTP_REST_SERVER.on("/system_alarm_state", HTTP_POST, []() {
      HTTP_REST_SERVER.send(200, "text/html", "Got system alarm state");
      onPostSystemAlarm();
    });
}

void handleMega2560Message(String message)
{
  if(message.startsWith("@V"))
  {
    if(message[2] == '1')
      postPersonVerified(true);
    else
      postPersonVerified(false);
  }
  if(message.startsWith("@A"))
  {
    if(message.charAt(2) != '\n' && message.charAt(2) != '\r' ) // The system alarm controller sends a new alarm state
    {
      message = message.substring(2);
      int alarm_state = message.toInt();
      postAlarmState(alarm_state);
    }
    else
    {
      String str_alarm_state = "@A" + String(getAlarmState());
      sendMessageToMega2560Serial(str_alarm_state.c_str());
    }
  }
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
  String mega2560Message;
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
  handleMega2560Message(mega2560Message);
}
