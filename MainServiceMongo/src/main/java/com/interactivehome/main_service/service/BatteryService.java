package com.interactivehome.main_service.service;

import com.interactivehome.main_service.model.dto.BatteryDto;
import com.interactivehome.main_service.model.entity.Battery;
import java.util.Date;
import java.util.List;


public interface BatteryService {
  void saveVoltage(BatteryDto dto);
  List<Battery> getVoltageByBatteryIdFromDateToDate(Integer batteryId, Date fromDate, Date toDate);
}
