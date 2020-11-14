package com.interactivehome.main_service.service.events;

import com.interactivehome.main_service.model.events.dto.RegisteredPersonDto;
import com.interactivehome.main_service.model.events.entity.RegisteredPerson;
import java.util.List;

public interface RegisteredPersonService {
  void savePerson(RegisteredPersonDto dto);
  List<RegisteredPerson> getAllRegisteredPersonsByAlarmId(Integer alarmId);
  RegisteredPerson getRegisteredPersonByAlarmIdAndPassword(Integer alarmId, String password);
  RegisteredPerson getRegisteredPersonByAlarmIdAndRfidCardId(Integer alarmId, String rfidCardId);
  String getRegisteredPersonNameByAlarmIdAndRfidCardId(Integer alarmId, String rfidCardId);
  String getRegisteredNamePersonByAlarmIdAndPassword(Integer alarmId, String password);
}
