package com.interactivehome.main_service.service.device;

import com.interactivehome.main_service.model.device.entity.DoorSensor;

import java.util.List;

public interface DoorSensorService {
    void addDoorSensor(DoorSensor doorSensor);
    void deleteDoorSensorByAlarmIdAndDoorId(Integer alarmId, Integer doorId);
    DoorSensor getDoorSensorByAlarmIdAndDoorId(Integer alarmId, Integer doorId);
    List<DoorSensor> getAllDoorSensorsByAlarmId(Integer alarmId);
}
