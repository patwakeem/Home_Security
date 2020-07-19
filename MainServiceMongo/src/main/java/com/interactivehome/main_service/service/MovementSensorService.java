package com.interactivehome.main_service.service;

import com.interactivehome.main_service.model.dto.MovementSensorDto;
import com.interactivehome.main_service.model.entity.MovementSensor;
import java.util.Date;
import java.util.List;

public interface MovementSensorService {
  void saveState(MovementSensorDto dto);
  List<MovementSensor> getMovementSensorActivityByMovementSensorId(Integer movementSensorId, Date fromDate, Date toDate);

  void buzzAlarm();
}
