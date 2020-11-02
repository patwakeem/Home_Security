// BatteryVoltage.h

#ifndef _BATTERYVOLTAGE_h
#define _BATTERYVOLTAGE_h

#if defined(ARDUINO) && ARDUINO >= 100
#include "Arduino.h"
#else
#include "WProgram.h"
#endif


float batteryVoltage(int analog_port, float r1, float r2, float max_voltage, int max_alalog_value, float correctionfactor)
{
  int max_measurements = 10.0;
  //For 0-25v voltage sensor
  float sum_v = 0.0;

  // two resistors 20K and 1k ohm
  for( int i = 0; i < max_measurements; ++i )
  {
    float vout = 0.0;
    float vin = 0.0;
    int value = 0; 
    // read the value at analog input 
    value = analogRead(analog_port); 
    vout = (value * max_voltage) / max_alalog_value; // see text 
    vin = vout / (r2/(r1+r2));

    if ( vin <= 0.0)
    {
      Serial.print("Battery Voltage = ");
      Serial.println(0.0 , 4);
      delay(500);
      return 0.0;
    }
    vin = vin - correctionfactor;
    sum_v += vin;
    delay(250);
  }
  
  Serial.print("Battery Voltage = ");
  Serial.println(sum_v / max_measurements , 4);
  float avg_v = sum_v / max_measurements;
  return ( avg_v < 0.0 ? 0.0 : avg_v );
}

#endif
