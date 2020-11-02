// DoorSensor.h

#ifndef _DOORSENSOR_h
#define _DOORSENSOR_h

#if defined(ARDUINO) && ARDUINO >= 100
#include "Arduino.h"
#else
#include "WProgram.h"
#endif

class DoorSensor
{
protected:
	int reed_pin;
public:
	DoorSensor(int pin) { reed_pin = pin; }
	~DoorSensor() {}

	void init() { pinMode(reed_pin, INPUT_PULLUP);	}
	bool is_door_closed();
};

#endif
