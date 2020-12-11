package com.interactivehome.main_service.service.events;

import com.interactivehome.main_service.model.events.dto.AlarmSystemStateDto;
import com.interactivehome.main_service.model.events.entity.AlarmSystemState;

import java.util.Date;
import java.util.List;

public interface AlarmSystemStateService {
  void saveAlarmStateById(Integer id, AlarmSystemStateDto dto);

  List<AlarmSystemState> getAlarmStateByIdFromDateToDate(Integer alarmId, Date fromDate, Date toDate);

  void stopAlarm(Integer id);
}
