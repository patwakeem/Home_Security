package com.interactivehome.main_service.service.device;

import com.interactivehome.main_service.model.device.dto.DoorSensorDto;
import com.interactivehome.main_service.model.device.dto.MovementSensorDto;
import com.interactivehome.main_service.model.device.entity.DoorSensor;
import com.interactivehome.main_service.model.device.entity.MovementSensor;

import java.util.List;

public interface MovementSensorService {
    void addMovementSensor(MovementSensorDto movementSensorDto);
    Integer getMovementSensorNewIdByAlarmId(Integer alarmId);
    void deleteMovementSensor(MovementSensorDto movementSensorDto);
    void modifyMovementSensor(MovementSensorDto movementSensorDto);
    MovementSensor getSensorByAlarmIdAndSensorId(Integer alarmId, Integer movementSensroId);
    List<MovementSensor> getAllMovementSensorsByAlarmId(Integer alarmId);
}
