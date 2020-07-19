package com.interactivehome.main_service.service;

import com.interactivehome.main_service.model.dto.AlarmDto;
import com.interactivehome.main_service.model.entity.Alarm;
import java.util.Date;
import java.util.List;

public interface AlarmService {
  void saveAlarmState(AlarmDto dto);

  Alarm getAlarmStateByAlarmId(Integer alarmId);

  List<Alarm> getAlarmStateByAlarmIdFromDateToDate(Integer alarmId, Date fromDate, Date toDate);

  void stopAlarm(AlarmDto dto);
}
