package com.interactivehome.main_service.service.device;


import com.interactivehome.main_service.model.device.dto.TemperatureHumidityGasSensorDto;
import com.interactivehome.main_service.model.device.entity.TemperatureHumidityGasSensor;

import java.util.List;

public interface TemperatureHumidityGasSensorService {
    void addTemperatureHumidityGasSensor(TemperatureHumidityGasSensorDto temperatureHumidityGasSensorDto);
    Integer getTemperatureHumidityGasSensorNewIdByAlarmId(Integer alarmId);
    void deleteTemperatureHumidityGasSensor(Integer alarmId, Integer sensorId);
    void modifyTemperatureHumidityGasSensor(TemperatureHumidityGasSensorDto temperatureHumidityGasSensorDto);
    TemperatureHumidityGasSensor getTemperatureHumidityGasSensorByAlarmIdAndSensorId(Integer alarmId, Integer temperatureHumidityGasId);
    List<TemperatureHumidityGasSensor> getAllTemperatureHumidityGasSensorsByAlarmId(Integer alarmId);
}
