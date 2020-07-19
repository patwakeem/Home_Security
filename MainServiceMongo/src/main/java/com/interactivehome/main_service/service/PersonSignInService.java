package com.interactivehome.main_service.service;

import com.interactivehome.main_service.model.dto.PersonSignInDto;
import com.interactivehome.main_service.model.entity.PersonSignIn;
import java.util.Date;
import java.util.List;

public interface PersonSignInService {
  void savePersonSingIn(PersonSignInDto dto);
  List<PersonSignIn> getPersonsSignedInByRfidCardIdFromDateToDate(String rfidCardId, Date fromDate, Date toDate);
}
