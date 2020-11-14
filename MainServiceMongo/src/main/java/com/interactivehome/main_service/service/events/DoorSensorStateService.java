package com.interactivehome.main_service.service.events;


import com.interactivehome.main_service.model.events.dto.DoorSensorStateDto;
import com.interactivehome.main_service.model.events.entity.DoorSensorState;
import java.util.Date;
import java.util.List;

public interface DoorSensorStateService {
  void saveState(DoorSensorStateDto dto);

  List<DoorSensorState> getDoorStateByAlarmIdAndSensorIdFromDateToDate(Integer alarmId, Integer sensorId, Date fromDate, Date toDate);
  DoorSensorState getLastDoorStateByAlarmIdAndSensorId(DoorSensorStateDto doorSensorStateDto);


  void saveBatteryVoltage(DoorSensorStateDto doorSensorStateDto);
}
