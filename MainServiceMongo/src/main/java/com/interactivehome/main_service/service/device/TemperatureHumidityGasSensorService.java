package com.interactivehome.main_service.service.device;


import com.interactivehome.main_service.model.device.entity.TemperatureHumidityGasSensor;

import java.util.List;

public interface TemperatureHumidityGasSensorService {
    void addTemperatureHumidityGasSensor(TemperatureHumidityGasSensor temperatureHumidityGasSensor);
    void deleteTemperatureHumidityGasSensorByAlarmIdAndDoorId(Integer alarmId, Integer temperatureHumidityGasId);
    TemperatureHumidityGasSensor getTemperatureHumidityGasSensorByAlarmIdAndDoorId(Integer alarmId, Integer temperatureHumidityGasId);
    List<TemperatureHumidityGasSensor> getAllTemperatureHumidityGasSensorsByAlarmId(Integer alarmId);
}
