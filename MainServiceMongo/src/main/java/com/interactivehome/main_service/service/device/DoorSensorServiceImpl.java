package com.interactivehome.main_service.service.device;

import com.interactivehome.main_service.model.device.dto.DoorSensorDto;
import com.interactivehome.main_service.model.device.entity.DoorSensor;
import com.interactivehome.main_service.model.device.entity.MovementSensor;
import com.interactivehome.main_service.repository.device.DoorSensorRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public void addDoorSensor(DoorSensorDto doorSensorDto) {
        DoorSensor doorSensor = new DoorSensor();
        doorSensorDto.sensorId = getDoorSensorNewIdByAlarmId(doorSensorDto.alarmId);
        doorSensor.mapFromDto(doorSensorDto);
        mongoTemplate.save(doorSensor);
    }

    @Override
    public Integer getDoorSensorNewIdByAlarmId(Integer alarmId) {
        Integer sensorId = -1;
        Query query = new Query();
        query.addCriteria(Criteria.where("alarm_id").is(alarmId));
        query.with(new org.springframework.data.domain.Sort(Sort.Direction.DESC, "sensor_id"));
        if(mongoTemplate.find(query, DoorSensor.class).isEmpty()) {
            return 1;
        } else {
            sensorId = mongoTemplate.find(query, DoorSensor.class).get(0).getSensorId() + 1;
        }
        return sensorId;
    }

    @Override
    public void deleteDoorSensor(DoorSensorDto doorSensorDto) {
        DoorSensor doorSensor = new DoorSensor();
        doorSensor.mapFromDto(doorSensorDto);
        mongoTemplate.remove(doorSensor);
    }

    @Override
    public void modifyDoorSensor(DoorSensorDto doorSensorDto) {
        DoorSensor doorSensor = new DoorSensor();
        Query query = new Query();
        query.addCriteria(Criteria.where("alarm_id").is(doorSensorDto.alarmId));
        query.addCriteria(Criteria.where("sensor_id").is(doorSensorDto.sensorId));
        doorSensor = mongoTemplate.findOne(query, DoorSensor.class);
        doorSensor.setAlarmId(doorSensorDto.getAlarmId());
        doorSensor.setSensorId(doorSensorDto.getSensorId());
        doorSensor.setEnabled(doorSensorDto.getEnabled());
        doorSensor.setDescription(doorSensorDto.getDescription());
        doorSensor.setDeviceIdentifier(doorSensorDto.getDeviceIdentifier());
        doorSensor.setBatteryPowered(doorSensorDto.getBatteryPowered());
        doorSensor.setArmIn(doorSensorDto.getArmIn());
        doorSensor.setArmAway(doorSensorDto.getArmAway());
        mongoTemplate.save(doorSensor);
    }

    @Override
    public DoorSensor getDoorSensorByAlarmIdAndSensorId(Integer alarmId, Integer sensorId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("alarm_id").is(alarmId));
        query.addCriteria(Criteria.where("sensor_id").is(sensorId));
        if(!mongoTemplate.find(query, DoorSensor.class).isEmpty()) {
            return mongoTemplate.findOne(query, DoorSensor.class);
        }
        return new DoorSensor();
    }

    @Override
    public List<DoorSensor> getAllDoorSensorsByAlarmId(Integer alarmId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("alarm_id").is(alarmId));
        if(!mongoTemplate.find(query, DoorSensor.class).isEmpty()) {
            return mongoTemplate.find(query, DoorSensor.class);
        }
        return new ArrayList<>();
    }
}
