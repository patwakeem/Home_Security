package com.interactivehome.main_service.service;


import com.interactivehome.main_service.model.dto.DoorSensorStateDto;
import com.interactivehome.main_service.model.entity.DoorSensorState;
import java.util.Date;
import java.util.List;

public interface DoorSensorStateService {
  void saveState(DoorSensorStateDto dto);

  List<DoorSensorState> getDoorStateByAlarmIdAndDoorIdFromDateToDate(Integer alarmId, Integer doorId, Date fromDate, Date toDate);
  DoorSensorState getLastDoorByAlarmIdAndDoorId(DoorSensorStateDto doorSensorStateDto);


  void saveBatteryVoltage(DoorSensorStateDto doorSensorStateDto);
}
