#include <ArduinoJson.h>
#include "WiFi.h"
#include "rom/rtc.h"
#include "driver/rtc_io.h"
#include "esp_sleep.h"
#include <esp_wifi.h>
#include <esp_bt.h>
#include "driver/adc.h"
#include <PIRSensor.h>
#include "C:\Development\Home_Projects\Arduino\Home_Security\Common\BatteryVoltage.h"
#include "C:\Development\Home_Projects\Arduino\Home_Security\Common\ESP32\HttpClientCommands.h"
#include "C:\Development\Home_Projects\Arduino\Home_Security\Common\ResetReason.h"
#include "C:\Development\Home_Projects\Arduino\Home_Security\Common\WifiConfiguration.h"
#include "C:\Development\Home_Projects\Arduino\Home_Security\Common\HomeServerSettings.h"
#include "C:\Development\Home_Projects\Arduino\Home_Security\Common\WifiCredentials.h"

#define BUTTON_PIN_BITMASK 0x0004 // GPIO 2

int movementSensorPin = 2;

const char* RES_MOVEMENT_SENSOR = "/movement_sensor";
int MOVEMENT_SENSOR_ID = 1;
int BATTERY_ID = 2;
#define _DEBUG

PIRSensor movementSensor(movementSensorPin);
/*
struct {
  byte data[4];
}rtc_data;

void print_data()
{
  char buf[3];
  uint8_t *ptr = (uint8_t *)&rtc_data;
  for (size_t i = 0; i < sizeof(rtc_data); i++)
  {
    sprintf(buf, "%02X", ptr[i]);
    Serial.print(buf);
    if ((i + 1) % 32 == 0) {
      Serial.println();
    } else {
      Serial.print(" ");
    }
  }
  Serial.println();
}

bool isFirstRun()
{
  rtc_data.data[0] = 0x00;
  rtc_data.data[1] = 0x00;
  rtc_data.data[2] = 0x00;
  rtc_data.data[3] = 0x00;
  if(ESP.rtcUserMemoryRead(65, (uint32_t*) &rtc_data, sizeof(rtc_data)))
  {
    Serial.println("Data read!");
    print_data();
  }


  if(rtc_data.data[0] == 0x00)
  {
    Serial.println("First run, going to deep sleep immediatelly...");
    rtc_data.data[0] = 0x01;
    rtc_data.data[1] = 0x00;
    rtc_data.data[2] = 0x00;
    rtc_data.data[3] = 0x00;
    if(ESP.rtcUserMemoryWrite(65, (uint32_t*) &rtc_data, sizeof(rtc_data)))
    {
      Serial.println("Data written!");
      print_data();
    }
    espDeepSleep();
  }

}
*/

// Voltage measurement
// 29/04/2020 23:16   -   3.970V  -  3.0μA
void espDeepSleep()
{
  esp_sleep_enable_ext1_wakeup(BUTTON_PIN_BITMASK, ESP_EXT1_WAKEUP_ALL_LOW);

  btStop();
  adc_power_off();
  esp_bt_controller_disable();

  esp_deep_sleep_start();
}

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

int getAlarmState()
{
  int statusCode = 0, retries = 0;
  bool validResponse = false;
  int alarmState = -1;
  while((statusCode != 200 || !validResponse) && retries < 3)
  {
    retries++;
    String response = "";
    statusCode = httpGet(HOME_SERVER_ADDRESS, HOME_SERVER_REST_PORT, "/alarm/1", response);
#ifdef _DEBUG
    Serial.print(String("Return code after calling: ") + String(HOME_SERVER_ADDRESS) + String(":") + String(HOME_SERVER_REST_PORT) + String("/alarm/1 statusCode: "));
    Serial.println(statusCode);
#endif
    if(statusCode != 200)
    {
      delay(100);
      continue;
    }
#ifdef _DEBUG
    Serial.print("Response body: ");
    Serial.println(response);
#endif
    StaticJsonBuffer<200> jsonBuffer;
    JsonObject& jsonBody = jsonBuffer.parseObject(response);
    if(jsonBody == JsonObject::invalid())
    {
      delay(100);
      continue;
    }
    validResponse = true;
    alarmState = jsonBody["alarm_state"];
#ifdef _DEBUG
    Serial.print("Alarm state: ");
    Serial.println(alarmState);
#endif
    if(statusCode != 200 || !validResponse)
      delay(100);
  }

  return alarmState;
}

int postMovementEvent(String& payload)
{
  StaticJsonBuffer<200> jsonBuffer;
  JsonObject& jsonObj = jsonBuffer.createObject();
  jsonObj["movement_sensor_id"] = MOVEMENT_SENSOR_ID;
  String body;
  jsonObj.printTo(body);
#ifdef _DEBUG
  Serial.print("Sensor body: ");
  Serial.println(body);
#endif
  int statusCode = httpPost(HOME_SERVER_ADDRESS, HOME_SERVER_REST_PORT, RES_MOVEMENT_SENSOR, body.c_str(), payload);
  Serial.print("Response code from home server after posting movement sensor event: ");
  Serial.println(statusCode);
  return statusCode;
}

int postBatteryVoltage(String& payload)
{
  StaticJsonBuffer<200> jsonBuffer;
  JsonObject& jsonObj = jsonBuffer.createObject();
  float voltage = batteryVoltage(A6, 30000, 7500, 3.3, 4095.0, -.71);//batteryVoltage(A0, 20000, 1000, 3.3, 4095.0, .01);
  jsonObj["battery_id"] = BATTERY_ID;
  jsonObj["battery_voltage"] = voltage;
  jsonObj["battery_percentage"] = (voltage - 3.0) * 100.0 / (4.2-3.0);
  String body;
  jsonObj.printTo(body);
#ifdef _DEBUG
  Serial.print("Battery body: ");
  Serial.println(body);
#endif
  int statusCode = httpPost(HOME_SERVER_ADDRESS, HOME_SERVER_REST_PORT, "/battery", body.c_str(), payload);
  return statusCode;
}

void setup()
{
  Serial.begin(115200);

  setCpuFrequencyMhz(80);
  adc_power_on();

#ifdef _DEBUG_2
  Serial.println("CPU0 reset reason:");
  print_reset_reason(rtc_get_reset_reason(0));
  verbose_print_reset_reason(rtc_get_reset_reason(0));

  Serial.println("CPU1 reset reason:");
  print_reset_reason(rtc_get_reset_reason(1));
  verbose_print_reset_reason(rtc_get_reset_reason(1));
#endif

  if (initWifi() == WL_CONNECTED) {
#ifdef _DEBUG
    Serial.println();
    Serial.print("Connected to ");
    Serial.print(WIFI_SSID);
    Serial.println();
    Serial.print("IP: ");
    Serial.println(WiFi.localIP());
#endif
  }
  else
  {
#ifdef _DEBUG
    Serial.print("Error connecting to: ");
    Serial.println(WIFI_SSID);
    Serial.println("Restarting...");
#endif
    ESP.restart();
  }

  pinMode(movementSensorPin, INPUT);
  if(getAlarmState() == 0/* || isFirstRun()*/)
  {
    String payload;
    postBatteryVoltage(payload);
#ifdef _DEBUG
//    Serial.println("Alarm is disarmed. Going to deep sleep immediatelly");
#endif
//    espDeepSleep();
  }
}

void loop()
{
  String payload;
  postMovementEvent(payload);
  postBatteryVoltage(payload);
  bool movement = movementSensor.isMovementCaught();
  Serial.print("sensor value: ");
  Serial.println(movement ? "movement" : "nothing");

//  if(movement <= 0)
//    espDeepSleep();
  delay(1000);
}
