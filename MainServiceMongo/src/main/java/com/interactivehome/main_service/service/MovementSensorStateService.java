package com.interactivehome.main_service.service;

import com.interactivehome.main_service.model.dto.MovementSensorStateDto;
import com.interactivehome.main_service.model.entity.MovementSensorState;
import java.util.Date;
import java.util.List;

public interface MovementSensorStateService {
  void saveState(MovementSensorStateDto dto);
  List<MovementSensorState> getSensorActivityByAlarmIdAndSensorId(Integer alarmId, Integer sensorId, Date fromDate, Date toDate);

  void buzzAlarm();
}
