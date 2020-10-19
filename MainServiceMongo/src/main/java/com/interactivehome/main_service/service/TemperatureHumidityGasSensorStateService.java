package com.interactivehome.main_service.service;

import com.interactivehome.main_service.model.dto.TemperatureHumidiryGasSensorStateDto;
import com.interactivehome.main_service.model.entity.TemperatureHumidityGasSensorState;
import java.util.Date;
import java.util.List;

public interface TemperatureHumidityGasSensorStateService {
  void saveValues(TemperatureHumidiryGasSensorStateDto dto);

  List<TemperatureHumidityGasSensorState> getValuesByAlarmIdAndSensorIdFromDateToDate(
      Integer alarmId, Integer sensorId, Date fromDate, Date toDate);
}
