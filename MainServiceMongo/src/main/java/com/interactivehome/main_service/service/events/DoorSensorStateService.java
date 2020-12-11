package com.interactivehome.main_service.service.events;


import com.interactivehome.main_service.model.events.dto.DoorSensorStateDto;
import com.interactivehome.main_service.model.events.entity.DoorSensorState;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

public interface DoorSensorStateService {
  ResponseEntity<String> saveStateByDoorSensorId(Integer id, Integer alarmState, DoorSensorStateDto dto);

  List<DoorSensorState> getDoorStateByAlarmIdAndSensorIdFromDateToDate(Integer alarmId, Integer sensorId, Date fromDate, Date toDate);
}
