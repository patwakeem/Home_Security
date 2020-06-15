float batteryVoltage()
{
  //For 0-25v voltage sensor
  float correctionfactor = -.84;
  float sum_v = 0.0;
   
  // two resistors 20K and 1k ohm
  float R1 = 30000;//20000;
  float R2 = 7500;//1000;
  for( int i = 0; i < 10; ++i )
  {
    float vout = 0.0;
    float vin = 0.0;
    int value = 0; 
    // read the value at analog input 
    value = analogRead(A6); 
    Serial.print("value: ");
    Serial.println(value);
    vout = (value * 3.3) / 4095.0; // see text 
    vin = vout / (R2/(R1+R2));
    Serial.print("vin: ");
    Serial.println(vin);

    if ( vin <= 0.0)
    {
      Serial.print("Battery Voltage = ");
      Serial.println(0.0 , 4);
      delay(2000);
      return 0.0;
    }
    vin = vin - correctionfactor;
    sum_v += vin;
    delay(500);
  }
  
  Serial.print("Battery Voltage = ");
  Serial.println(sum_v / 10.0 , 4);
  float avg_v = sum_v / 10.0;
  return ( avg_v < 0.0 ? 0.0 : avg_v );
}

void setup() {
 
  // initialize serial communication at 9600 bits per second:
 
  Serial.begin(9600);
 
}
 
 
 
// the loop routine runs over and over again forever:
 
void loop() {
batteryVoltage();
}
