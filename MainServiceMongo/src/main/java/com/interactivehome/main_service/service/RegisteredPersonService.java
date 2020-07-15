package com.interactivehome.main_service.service;

import com.interactivehome.main_service.model.dto.RegisteredPersonDto;
import com.interactivehome.main_service.model.entity.RegisteredPerson;
import java.util.List;

public interface RegisteredPersonService {
  void savePerson(RegisteredPersonDto dto);
  List<RegisteredPerson> getAllRegisteredPersons();
  RegisteredPerson getRegisteredPersonByRfidCardId(String rfidCardId);
  RegisteredPerson getRegisteredPersonByPassword(String password);
}
