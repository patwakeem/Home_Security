package com.interactivehome.main_service.controller.events;

import com.interactivehome.main_service.model.events.dto.RegisteredPersonDto;
import com.interactivehome.main_service.model.events.entity.RegisteredPerson;
import com.interactivehome.main_service.service.events.RegisteredPersonService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class RegisterPersonController {

  private final RegisteredPersonService registeredPersonService;

  RegisterPersonController(RegisteredPersonService registeredPersonService) {
    this.registeredPersonService = registeredPersonService;
  }

  @PostMapping("/register_person")
  public ResponseEntity<String> postRegisterPerson(@RequestBody RegisteredPersonDto dto) {
    registeredPersonService.savePerson(dto);
    return ResponseEntity.ok("201");
  }

  @GetMapping("/persons/{alarmId}")
  public List<RegisteredPerson> getRegisteredPersonsByAlarmId(@PathVariable Integer alarmId) {
    return registeredPersonService.getAllRegisteredPersonsByAlarmId(alarmId);
  }

  @GetMapping("/person_rfid_card/{alarmId}/{rfidCardId}")
  public String getRegisteredPersonByAlarmIdAndRfidCard(@PathVariable Integer alarmId, @PathVariable String rfidCardId) {
    return registeredPersonService.getRegisteredPersonNameByAlarmIdAndRfidCardId(alarmId, rfidCardId);
  }

  @GetMapping("/person_password/{alarmId}/{password}")
  public String getRegisteredPersonByAlarmIdAndPassword(@PathVariable Integer alarmId, @PathVariable String password) {
    return registeredPersonService.getRegisteredNamePersonByAlarmIdAndPassword(alarmId, password);
  }
}
