package com.interactivehome.main_service.service;

import com.interactivehome.main_service.model.dto.BatteryDto;


public interface BatteryService {
  void saveVoltage(BatteryDto dto);
  BatteryDto getVoltageByBatteryId(Integer batteryId);
}
