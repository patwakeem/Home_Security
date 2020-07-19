package com.interactivehome.main_service.service;

import com.interactivehome.main_service.model.dto.TemperatureHumidiryGasDto;
import com.interactivehome.main_service.model.entity.TemperatureHumidityGas;
import java.util.Date;
import java.util.List;

public interface TemperatureHumidityGasService {
  void saveValues(TemperatureHumidiryGasDto dto);

  List<TemperatureHumidityGas> getValuesBySensorIdFromDateToDate(
      Integer sensorId, Date fromDate, Date toDate);
}
