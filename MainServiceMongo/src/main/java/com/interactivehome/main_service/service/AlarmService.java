package com.interactivehome.main_service.service;

import com.interactivehome.main_service.model.dto.AlarmDto;
import com.interactivehome.main_service.model.entity.Alarm;
import java.util.Date;
import java.util.List;

public interface AlarmService {
  void saveAlarmState(AlarmDto dto);

  Integer getAlarmStateByAlarmId(Integer alarmId);

  Boolean getAlarmOnByAlarmId(Integer alarmId);

  List<Alarm> getAlarmByAlarmIdFromDateToDate(Integer alarmId, Date fromDate, Date toDate);

  void stopAlarm(AlarmDto dto);

}
