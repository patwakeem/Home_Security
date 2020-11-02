package com.interactivehome.main_service.service.device;

import com.interactivehome.main_service.model.device.dto.DoorSensorDto;
import com.interactivehome.main_service.model.device.entity.DoorSensor;

import java.util.List;

public interface DoorSensorService {
    void addDoorSensor(DoorSensorDto doorSensorDto);
    Integer getDoorSensorNewIdByAlarmId(Integer alarmId);
    void deleteDoorSensor(DoorSensorDto doorSensorDto);
    void modifyDoorSensor(DoorSensorDto doorSensorDto);
    DoorSensor getDoorSensorByAlarmIdAndSensorId(Integer alarmId, Integer sensorId);
    List<DoorSensor> getAllDoorSensorsByAlarmId(Integer alarmId);
}
