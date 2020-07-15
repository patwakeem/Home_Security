package com.interactivehome.main_service.service;

import com.interactivehome.main_service.model.dto.MovementSensorDto;
import com.interactivehome.main_service.model.entity.MovementSensor;

public interface MovementSensorService {
  void saveState(MovementSensorDto dto);
  MovementSensor getMovementSensorStateByMovementSensorId(Integer movementSensorId);

  void buzzAlarm();
}
