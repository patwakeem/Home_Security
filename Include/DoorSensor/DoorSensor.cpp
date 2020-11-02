#include "DoorSensor.h"
//#define DEBUG

bool DoorSensor::is_door_closed()
{
	uint16_t readval = digitalRead(reed_pin); // Check the pin
	if (readval > 0)
	{
		//lcd.display_message("Door is opened!", 1);
#ifdef DEBUG
		Serial.print("Door Sensor read value: ");
		Serial.println(readval);
		Serial.println("Door open!");
#endif
		return false;
	}
	else
	{
		return true;
	}
}
