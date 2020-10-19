package com.interactivehome.main_service.service;

import com.interactivehome.main_service.model.dto.AlarmStateDto;
import com.interactivehome.main_service.model.entity.Alarm;
import java.util.Date;
import java.util.List;

public interface AlarmService {
  void saveAlarmState(AlarmStateDto dto);

  Integer getAlarmStateByAlarmId(Integer alarmId);

  Boolean getAlarmOnByAlarmId(Integer alarmId);

  List<Alarm> getAlarmByAlarmIdFromDateToDate(Integer alarmId, Date fromDate, Date toDate);

  void stopAlarm(AlarmStateDto dto);

}
