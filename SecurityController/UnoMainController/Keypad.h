// keypad.h

#ifndef _KEYPAD_h
#define _KEYPAD_h

#if defined(ARDUINO) && ARDUINO >= 100
#include "Arduino.h"
#else
#include "WProgram.h"
#endif

#include <Keypad.h>
#include <vector>

class KPad {
  public:
    KPad(int colns, int rows) {}
  private:
    int colns;
    int rows;
    std::vector<int> colPins;
    std::vector<int> rowPins;
};


#endif
