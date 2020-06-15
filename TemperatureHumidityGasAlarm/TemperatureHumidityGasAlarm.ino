/*****************************************************
* name:Humiture Detection
* function:you can see the current value of humidity and temperature displayed on the I2C LCD1602.
******************************************************/
//Email:support@sunfounder.com
//Website:www.sunfounder.com

//include the libraries
#include <stdio.h>
#include <ESP8266WebServer.h>
#include <ESP8266HTTPClient.h>
#include <ArduinoJson.h>
#include <DHT.h>
#include "C:\Development\Home_Projects\Arduino\Home_Security\Common\HttpClientCommands.h"
#include "C:\Development\Home_Projects\Arduino\Home_Security\Common\WifiConfiguration.h"
#include "C:\Development\Home_Projects\Arduino\Home_Security\Common\HomeServerSettings.h"
#include "C:\Development\Home_Projects\Arduino\Home_Security\Common\WifiCredentials.h"

//REST server settings
const int REST_SERVER_PORT = 8080;

const int DHT11_PIN= 2;//Humiture sensor attach to pin7
const int GAS_PIN = A0;
const int SECURITY_ALARM_PIN = 12;
const int GAS_FIRE_ALARM_PIN = 13;

volatile int SECURITY_ALARM_ON = 0;
volatile int GAS_FIRE_ALARM_ON = 0;

//Rest Server Resources
const char* RES_SENSORS_VALUES = "/sensors_values";


#define DHTTYPE DHT11     // DHT 11
//#define DHTTYPE DHT22   // DHT 22, AM2302, AM2321
//#define DHTTYPE DHT21   // DHT 21, AM2301

DHT dht(DHT11_PIN, DHTTYPE);

ESP8266WebServer HTTP_REST_SERVER(REST_SERVER_PORT);

unsigned long LAST_POST_MILLIS;
const unsigned int PERIOD = 3 * 1000; // check values every 10 sec

float LAST_TEMPERATURE_VAL;
float LAST_HUMIDITY_VAL;
int LAST_GAS_VAL;


int initWifi()
{
  int retries = 0;

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
  SECURITY_ALARM_ON = jsonBody["alarm_on"];
  GAS_FIRE_ALARM_ON = jsonBody["gas_fire_alarm_on"];
}

void onPutTriggerAlarm()
{
  StaticJsonBuffer<500> jsonBuffer;
  String post_body = HTTP_REST_SERVER.arg("plain");
  Serial.println(post_body);

  JsonObject& jsonBody = jsonBuffer.parseObject(HTTP_REST_SERVER.arg("plain"));

  Serial.print("HTTP Method: ");
  Serial.println(HTTP_REST_SERVER.method());
  
  if (!jsonBody.success()) {
    Serial.println("error parsing json body");
    HTTP_REST_SERVER.send(400);
  }
  else 
  {   
    if (HTTP_REST_SERVER.method() == HTTP_PUT)
    {
      json_to_resource(jsonBody);
      HTTP_REST_SERVER.sendHeader("Location", "/trigger_alarm");
      HTTP_REST_SERVER.send(200);
    }
    else
    {
      HTTP_REST_SERVER.send(404);
    }
  }
}

void onEspRestart()
{
  ESP.restart();
}

void configRestServerRouting() {
    HTTP_REST_SERVER.on("/", HTTP_GET, []() {
        HTTP_REST_SERVER.send(200, "text/html",
            "Hello from Alarm controller REST Web Server");
    });

    HTTP_REST_SERVER.on("/trigger_alarm", HTTP_PUT, onPutTriggerAlarm);
    HTTP_REST_SERVER.on("/esp_restart", HTTP_GET, onEspRestart);
}

void triggerAlarm() {
  if(SECURITY_ALARM_ON == 1)
  {
    digitalWrite(SECURITY_ALARM_PIN, HIGH);
    delay(300); //wait for 5 milliseconds 
    digitalWrite(SECURITY_ALARM_PIN, LOW);
    delay(300); //wait for 5 milliseconds 
  }
  else
    digitalWrite(SECURITY_ALARM_PIN, LOW);
}

void buzzCorrectPassword() {
  tone(GAS_FIRE_ALARM_PIN, 500); //turn the buzzer on
  delay(200); //wait for 5 milliseconds 
  tone(GAS_FIRE_ALARM_PIN, 300); //turn the buzzer on
  delay(500); //wait for 5 milliseconds 
  noTone(GAS_FIRE_ALARM_PIN);
  delay(3000);
}

void triggerGasFireAlarm() {
  if(GAS_FIRE_ALARM_ON == 0)
  {
    noTone(GAS_FIRE_ALARM_PIN);
    return;
  }
  
  for(int t = 0; t < 3; t++) {
    //frequence loop from 200 to 800
    for(int i = 200;i <= 300;i++) {
      tone(GAS_FIRE_ALARM_PIN,i); //turn the buzzer on
      delay(1); //wait for 5 milliseconds 
    }
    delay(50); //wait for 4 seconds on highest frequence
    //frequence loop from 800 downto 200
    for(int i = 300;i >= 200;i--) {
      tone(GAS_FIRE_ALARM_PIN,i);
      delay(3);
    }
  }
  noTone(GAS_FIRE_ALARM_PIN);
}

void postSensorsValues()
{
  unsigned long current_millis = millis();
  if(current_millis < LAST_POST_MILLIS + PERIOD)
    return;

  float h=0.0,t=0.0;
  int g=0;
  int n_measurements=10;
  for(int i = 0; i < n_measurements; ++i)
  {
    t += dht.readTemperature();
    h += dht.readHumidity();
    g += analogRead(GAS_PIN);
    delay(250);
  }
  float temperature = t / n_measurements;
  float humidity = h / n_measurements;  
  int gas = g / n_measurements;

  if(gas >= 2)
    SECURITY_ALARM_ON = 1;
  else
    SECURITY_ALARM_ON = 0;

  LAST_POST_MILLIS = millis();
  if(LAST_TEMPERATURE_VAL == temperature && LAST_HUMIDITY_VAL == humidity && LAST_GAS_VAL == gas)
    return;

  LAST_TEMPERATURE_VAL = temperature;
  LAST_HUMIDITY_VAL = humidity;
  LAST_GAS_VAL = gas;

  char str_temperature[6];
  char str_humidity[6];
  char str_gas[6];

  snprintf(str_temperature, sizeof str_temperature, "%.2f}", temperature);
  snprintf(str_humidity, sizeof str_humidity, "%.2f", humidity);
  snprintf(str_gas, sizeof str_gas, "%d", gas);

  Serial.print("Temperature = ");
  Serial.print(temperature);
  Serial.println(" degree celsius");
  Serial.print("Humidity = ");
  Serial.print(humidity);
  Serial.println("%");
  Serial.print("gas: ");
  Serial.println(gas);

  StaticJsonBuffer<200> jsonBuffer;
  JsonObject& jsonObj = jsonBuffer.createObject();
  jsonObj["temperature"] = temperature;
  jsonObj["humidity"] = humidity;
  jsonObj["gasValue"] = gas;
  String body;
  jsonObj.printTo(body);
  Serial.print("Sensors body: ");
  Serial.println(body);
  String payload;
  int statusCode = httpPost(HOME_SERVER_ADDRESS, String(HOME_SERVER_REST_PORT), RES_SENSORS_VALUES, body, payload);
}

void setup() {
  Serial.begin(115200);

  Serial.println();
  if (initWifi() == WL_CONNECTED) {
    Serial.print("Connected to ");
    Serial.print(WIFI_SSID);
    Serial.print("--- IP: ");
    Serial.println(WiFi.localIP());
  }
  else {
    Serial.print("Error connecting to: ");
    Serial.println(WIFI_SSID);
    Serial.println("Restarting...");
    delay(1000);
    ESP.restart();
  }

  configRestServerRouting();

  HTTP_REST_SERVER.begin();
  Serial.println("HTTP REST Server Started");

  pinMode(SECURITY_ALARM_PIN, OUTPUT);
  digitalWrite(SECURITY_ALARM_PIN, LOW);
  pinMode(GAS_FIRE_ALARM_PIN, OUTPUT);
  noTone(GAS_FIRE_ALARM_PIN);
  pinMode(GAS_PIN, INPUT);


  LAST_POST_MILLIS = millis();

  dht.begin();

  LAST_TEMPERATURE_VAL = 0;
  LAST_HUMIDITY_VAL = 0;
  LAST_GAS_VAL = 0;
}

void loop() {
  delay(10);
  HTTP_REST_SERVER.handleClient();
  triggerGasFireAlarm();
  triggerAlarm();
  postSensorsValues();
}
