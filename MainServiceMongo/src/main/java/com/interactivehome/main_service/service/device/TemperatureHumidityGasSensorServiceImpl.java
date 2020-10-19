package com.interactivehome.main_service.service.device;

import com.interactivehome.main_service.model.device.entity.TemperatureHumidityGasSensor;
import com.interactivehome.main_service.repository.device.DoorSensorRepository;
import com.interactivehome.main_service.repository.device.TemperatureHumidityGasSensorRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

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
    public void addTemperatureHumidityGasSensor(TemperatureHumidityGasSensor temperatureHumidityGasSensor) {

    }

    @Override
    public void deleteTemperatureHumidityGasSensorByAlarmIdAndDoorId(Integer alarmId, Integer temperatureHumidityGasId) {

    }

    @Override
    public TemperatureHumidityGasSensor getTemperatureHumidityGasSensorByAlarmIdAndDoorId(Integer alarmId, Integer temperatureHumidityGasId) {
        return null;
    }

    @Override
    public List<TemperatureHumidityGasSensor> getAllTemperatureHumidityGasSensorsByAlarmId(Integer alarmId) {
        return null;
    }
}
