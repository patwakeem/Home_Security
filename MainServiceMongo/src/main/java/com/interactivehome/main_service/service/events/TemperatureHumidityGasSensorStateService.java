package com.interactivehome.main_service.service.events;

import com.interactivehome.main_service.model.events.dto.TemperatureHumidiryGasSensorStateDto;
import com.interactivehome.main_service.model.events.entity.TemperatureHumidityGasSensorState;
import java.util.Date;
import java.util.List;

public interface TemperatureHumidityGasSensorStateService {
  void saveValues(TemperatureHumidiryGasSensorStateDto dto);

  List<TemperatureHumidityGasSensorState> getValuesByAlarmIdAndSensorIdFromDateToDate(
      Integer alarmId, Integer sensorId, Date fromDate, Date toDate);
}
