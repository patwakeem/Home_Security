// home_server_settings.h

#ifndef _HOMESERVERSETTINGS_h
#define _HOMESERVERSETTINGS_h

#if defined(ARDUINO) && ARDUINO >= 100
#include "Arduino.h"
#else
#include "WProgram.h"
#endif

//Home Server settings
const char* HOME_SERVER_ADDRESS = "192.168.1.159";
const int   HOME_SERVER_REST_PORT = 8080;

#endif
