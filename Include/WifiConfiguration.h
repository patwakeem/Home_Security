// wifi_configuration.h

#ifndef _WIFICONFIGURATION_h
#define _WIFICONFIGURATION_h

#if defined(ARDUINO) && ARDUINO >= 100
#include "Arduino.h"
#else
#include "WProgram.h"
#endif

//Wifi configuration
const int WIFI_RETRY_DELAY_MS = 1000;
const int MAX_WIFI_INIT_RETRY = 15;
const int VERIFY_TIMEOUT_SEC = 15;

#endif
