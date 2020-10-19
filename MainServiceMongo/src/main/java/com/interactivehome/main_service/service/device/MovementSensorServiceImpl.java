package com.interactivehome.main_service.service.device;

import com.interactivehome.main_service.model.device.entity.MovementSensor;
import com.interactivehome.main_service.repository.device.MovementSensorRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovementSensorServiceImpl implements MovementSensorService {

    private final MovementSensorRepository movementSensorRepository;
    private final MongoTemplate mongoTemplate;

    public MovementSensorServiceImpl(MovementSensorRepository movementSensorRepository,
                                 MongoTemplate mongoTemplate) {
        this.movementSensorRepository = movementSensorRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void addMovementSensor(MovementSensor movementSensor) {

    }

    @Override
    public void deleteMovementSensorByAlarmIdAndDoorId(Integer alarmId, Integer movementSensorId) {

    }

    @Override
    public MovementSensor getMovementSensorByAlarmIdAndDoorId(Integer alarmId, Integer movementSensorId) {
        return null;
    }

    @Override
    public List<MovementSensor> getAllMovementSensorsByAlarmId(Integer alarmId) {
        return null;
    }
}
