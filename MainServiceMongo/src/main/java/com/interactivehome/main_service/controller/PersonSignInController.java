package com.interactivehome.main_service.controller;

import com.interactivehome.main_service.model.dto.PersonSignInDto;
import com.interactivehome.main_service.model.entity.PersonSignIn;
import com.interactivehome.main_service.service.PersonSignInService;
import java.util.Date;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class PersonSignInController {
  private final PersonSignInService personSignInService;

  PersonSignInController(PersonSignInService personSignInService) {
    this.personSignInService = personSignInService;
  }

  @PostMapping("/person_signin")
  public ResponseEntity<String> postPersonSingIn(@RequestBody PersonSignInDto dto) {
    personSignInService.savePersonSingIn(dto);
    return ResponseEntity.ok("201");
  }

  @GetMapping("/person_signin/{alarmId}/{rfidCardId}")
  public List<PersonSignIn> getAllPersonSignInByRfidCardIdFromDateToDate(
      @PathVariable Integer alarmId,
      @PathVariable String rfidCardId,
      @RequestParam(value = "fromDate", required = false, defaultValue = "2020-07-01") @DateTimeFormat(pattern="yyyy-MM-dd") Date fromDate,
      @RequestParam(value = "toDate", required = false, defaultValue = "2020-07-14") @DateTimeFormat(pattern="yyyy-MM-dd") Date toDate)
  {
    return personSignInService.getPersonsSignedInByAlarmIdAndRfidCardIdFromDateToDate(alarmId, rfidCardId, fromDate, toDate);
  }
}
