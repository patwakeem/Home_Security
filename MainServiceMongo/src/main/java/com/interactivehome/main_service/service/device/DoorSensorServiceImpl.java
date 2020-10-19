package com.interactivehome.main_service.service.device;

import com.interactivehome.main_service.model.device.entity.DoorSensor;
import com.interactivehome.main_service.repository.device.DoorSensorRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoorSensorServiceImpl implements DoorSensorService {

    private final DoorSensorRepository doorSensorRepository;
    private final MongoTemplate mongoTemplate;

    public DoorSensorServiceImpl(DoorSensorRepository doorSensorRepository,
                                      MongoTemplate mongoTemplate) {
        this.doorSensorRepository = doorSensorRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void addDoorSensor(DoorSensor doorSensor) {

    }

    @Override
    public void deleteDoorSensorByAlarmIdAndDoorId(Integer alarmId, Integer doorId) {

    }

    @Override
    public DoorSensor getDoorSensorByAlarmIdAndDoorId(Integer alarmId, Integer doorId) {
        return null;
    }

    @Override
    public List<DoorSensor> getAllDoorSensorsByAlarmId(Integer alarmId) {
        return null;
    }
}
