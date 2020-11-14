package com.interactivehome.main_service.service.device;

import com.interactivehome.main_service.model.device.dto.TemperatureHumidityGasSensorDto;
import com.interactivehome.main_service.model.device.entity.TemperatureHumidityGasSensor;
import com.interactivehome.main_service.model.events.entity.TemperatureHumidityGasSensorState;
import com.interactivehome.main_service.repository.device.TemperatureHumidityGasSensorRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TemperatureHumidityGasSensorServiceImpl implements TemperatureHumidityGasSensorService {

    private final TemperatureHumidityGasSensorRepository temperatureHumidityGasSensorRepository;
    private final MongoTemplate mongoTemplate;

    public TemperatureHumidityGasSensorServiceImpl(TemperatureHumidityGasSensorRepository temperatureHumidityGasSensorRepository,
                                 MongoTemplate mongoTemplate) {
        this.temperatureHumidityGasSensorRepository = temperatureHumidityGasSensorRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void addTemperatureHumidityGasSensor(TemperatureHumidityGasSensorDto temperatureHumidityGasSensorDto) {
        TemperatureHumidityGasSensor temperatureHumidityGasSensor = new TemperatureHumidityGasSensor();
        temperatureHumidityGasSensorDto.sensorId = getTemperatureHumidityGasSensorNewIdByAlarmId(temperatureHumidityGasSensorDto.alarmId);
        temperatureHumidityGasSensor.mapFromDto(temperatureHumidityGasSensorDto);
        mongoTemplate.save(temperatureHumidityGasSensor);
    }

    @Override
    public Integer getTemperatureHumidityGasSensorNewIdByAlarmId(Integer alarmId) {
        Integer temperatureHumidityGasId = -1;
        Query query = new Query();
        query.addCriteria(Criteria.where("alarm_id").is(alarmId));
        query.with(new org.springframework.data.domain.Sort(Sort.Direction.DESC, "sensor_id"));
        if(mongoTemplate.find(query, TemperatureHumidityGasSensor.class).isEmpty()) {
            return 1;
        } else {
            temperatureHumidityGasId = mongoTemplate.find(query, TemperatureHumidityGasSensor.class).get(0).getSensorId() + 1;
        }
        return temperatureHumidityGasId;
    }

    @Override
    public void deleteTemperatureHumidityGasSensor(Integer alarmId, Integer sensorId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("alarm_id").is(alarmId));
        query.addCriteria(Criteria.where("sensor_id").is(sensorId));
        mongoTemplate.findAndRemove(query, TemperatureHumidityGasSensor.class);
    }

    @Override
    public void modifyTemperatureHumidityGasSensor(TemperatureHumidityGasSensorDto temperatureHumidityGasSensorDto) {
        TemperatureHumidityGasSensor temperatureHumidityGasSensor = new TemperatureHumidityGasSensor();
        Query query = new Query();
        query.addCriteria(Criteria.where("alarm_id").is(temperatureHumidityGasSensorDto.alarmId));
        query.addCriteria(Criteria.where("sensor_id").is(temperatureHumidityGasSensorDto.sensorId));
        temperatureHumidityGasSensor = mongoTemplate.findOne(query, TemperatureHumidityGasSensor.class);
        temperatureHumidityGasSensor.setAlarmId(temperatureHumidityGasSensorDto.getAlarmId());
        temperatureHumidityGasSensor.setSensorId(temperatureHumidityGasSensorDto.getSensorId());
        temperatureHumidityGasSensor.setEnabled(temperatureHumidityGasSensorDto.getEnabled());
        temperatureHumidityGasSensor.setDescription(temperatureHumidityGasSensorDto.getDescription());
        temperatureHumidityGasSensor.setDeviceIdentifier(temperatureHumidityGasSensorDto.getDeviceIdentifier());
        temperatureHumidityGasSensor.setBatteryPowered(temperatureHumidityGasSensorDto.getBatteryPowered());
        temperatureHumidityGasSensor.setArmIn(temperatureHumidityGasSensorDto.getArmIn());
        temperatureHumidityGasSensor.setArmAway(temperatureHumidityGasSensorDto.getArmAway());
        mongoTemplate.save(temperatureHumidityGasSensor);
    }

    @Override
    public TemperatureHumidityGasSensor getTemperatureHumidityGasSensorByAlarmIdAndSensorId(Integer alarmId, Integer temperatureHumidityGasId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("alarm_id").is(alarmId));
        query.addCriteria(Criteria.where("sensor_id").is(temperatureHumidityGasId));
        if(!mongoTemplate.find(query, TemperatureHumidityGasSensor.class).isEmpty()) {
            return mongoTemplate.findOne(query, TemperatureHumidityGasSensor.class);
        }
        return null;
    }

    @Override
    public List<TemperatureHumidityGasSensor> getAllTemperatureHumidityGasSensorsByAlarmId(Integer alarmId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("alarm_id").is(alarmId));
        List<TemperatureHumidityGasSensor> temperatureHumidityGasSensorList = new ArrayList<>();
        if(mongoTemplate.find(query, TemperatureHumidityGasSensorState.class).size() > 0)
            return mongoTemplate.find(query, TemperatureHumidityGasSensor.class);

        return new ArrayList<>();
    }
}
