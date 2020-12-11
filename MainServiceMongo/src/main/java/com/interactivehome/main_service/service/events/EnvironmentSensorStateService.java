package com.interactivehome.main_service.service.events;

import com.interactivehome.main_service.model.events.dto.EnvironmentSensorStateDto;
import com.interactivehome.main_service.model.events.entity.EnvironmentSensorState;
import java.util.Date;
import java.util.List;

public interface EnvironmentSensorStateService {
  void saveValuesBySensorId(Integer id, EnvironmentSensorStateDto dto);

  List<EnvironmentSensorState> getValuesByAlarmIdAndSensorIdFromDateToDate(
      Integer alarmId, Integer sensorId, Date fromDate, Date toDate);
}
