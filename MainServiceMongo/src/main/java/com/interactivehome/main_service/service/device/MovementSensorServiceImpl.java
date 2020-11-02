package com.interactivehome.main_service.service.device;

import com.interactivehome.main_service.model.device.dto.MovementSensorDto;
import com.interactivehome.main_service.model.device.entity.MovementSensor;
import com.interactivehome.main_service.model.device.entity.TemperatureHumidityGasSensor;
import com.interactivehome.main_service.repository.device.MovementSensorRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public void addMovementSensor(MovementSensorDto movementSensorDto) {
        MovementSensor movementSensor = new MovementSensor();
        movementSensorDto.sensorId = getMovementSensorNewIdByAlarmId(movementSensorDto.alarmId);
        movementSensor.mapFromDto(movementSensorDto);
        mongoTemplate.save(movementSensor);
    }

    @Override
    public Integer getMovementSensorNewIdByAlarmId(Integer alarmId) {
        Integer sensorId = -1;
        Query query = new Query();
        query.addCriteria(Criteria.where("alarm_id").is(alarmId));
        query.with(new org.springframework.data.domain.Sort(Sort.Direction.DESC, "sensor_id"));
        if(mongoTemplate.find(query, MovementSensor.class).isEmpty()) {
            return 1;
        } else {
            sensorId = mongoTemplate.find(query, MovementSensor.class).get(0).getSensorId() + 1;
        }
        return sensorId;
    }

    @Override
    public void deleteMovementSensor(MovementSensorDto movementSensorDto) {
        MovementSensor movementSensor = new MovementSensor();
        movementSensor.mapFromDto(movementSensorDto);
        mongoTemplate.remove(movementSensor);
    }

    @Override
    public void modifyMovementSensor(MovementSensorDto movementSensorDto) {
        MovementSensor movementSensor = new MovementSensor();
        Query query = new Query();
        query.addCriteria(Criteria.where("alarm_id").is(movementSensorDto.alarmId));
        query.addCriteria(Criteria.where("sensor_id").is(movementSensorDto.sensorId));
        movementSensor = mongoTemplate.findOne(query, MovementSensor.class);
        movementSensor.setAlarmId(movementSensorDto.getAlarmId());
        movementSensor.setSensorId(movementSensorDto.getSensorId());
        movementSensor.setDescription(movementSensorDto.getDescription());
        movementSensor.setDeviceIdentifier(movementSensorDto.getDeviceIdentifier());
        movementSensor.setEnabled(movementSensorDto.getEnabled());
        movementSensor.setBatteryPowered(movementSensorDto.getBatteryPowered());
        movementSensor.setArmIn(movementSensorDto.getArmIn());
        movementSensor.setArmAway(movementSensorDto.getArmAway());
        mongoTemplate.save(movementSensor);
    }

    @Override
    public MovementSensor getSensorByAlarmIdAndSensorId(Integer alarmId, Integer sensorId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("alarm_id").is(alarmId));
        query.addCriteria(Criteria.where("sensor_id").is(sensorId));
        if(!mongoTemplate.find(query, MovementSensor.class).isEmpty()) {
            return mongoTemplate.findOne(query, MovementSensor.class);
        }
        return new MovementSensor();
    }

    @Override
    public List<MovementSensor> getAllMovementSensorsByAlarmId(Integer alarmId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("alarm_id").is(alarmId));
        if(!mongoTemplate.find(query, MovementSensor.class).isEmpty()) {
            return mongoTemplate.find(query, MovementSensor.class);
        }
        return new ArrayList<>();
    }
}
