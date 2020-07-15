package com.interactivehome.main_service.controller;

import com.interactivehome.main_service.model.dto.RegisteredPersonDto;
import com.interactivehome.main_service.model.entity.RegisteredPerson;
import com.interactivehome.main_service.service.RegisteredPersonService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

  @GetMapping("/persons")
  public List<RegisteredPerson> getRegisteredPersons() {
    return registeredPersonService.getAllRegisteredPersons();
  }

  @GetMapping("/person_rfid_card/{rfidCardId}")
  public RegisteredPerson getRegisteredPersonByRfidCard(@PathVariable String rfidCardId) {
    return registeredPersonService.getRegisteredPersonByRfidCardId(rfidCardId);
  }

  @GetMapping("/person_password/{password}")
  public RegisteredPerson getRegisteredPersonByPassword(@PathVariable String password) {
    return registeredPersonService.getRegisteredPersonByPassword(password);
  }
}
