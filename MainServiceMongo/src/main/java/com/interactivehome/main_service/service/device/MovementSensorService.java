package com.interactivehome.main_service.service.device;

import com.interactivehome.main_service.model.device.entity.MovementSensor;

import java.util.List;

public interface MovementSensorService {
    void addMovementSensor(MovementSensor movementSensor);
    void deleteMovementSensorByAlarmIdAndDoorId(Integer alarmId, Integer movementSensorId);
    MovementSensor getMovementSensorByAlarmIdAndDoorId(Integer alarmId, Integer movementSensorId);
    List<MovementSensor> getAllMovementSensorsByAlarmId(Integer alarmId);
}
