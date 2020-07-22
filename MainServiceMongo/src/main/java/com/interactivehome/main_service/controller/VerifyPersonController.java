package com.interactivehome.main_service.controller;

import com.interactivehome.main_service.model.dto.PasswordDto;
import com.interactivehome.main_service.model.dto.RfidCardDto;
import com.interactivehome.main_service.service.RegisteredPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class VerifyPersonController {

  @Autowired
  private RegisteredPersonService registeredPersonService;

  @PostMapping(value = "/verify_person_by_rfid_card")
  public ResponseEntity<String> getRegisteredPersonByRfidCardId(@RequestBody RfidCardDto dto) throws JSONException {
    String personName = registeredPersonService.getRegisteredPersonNameByRfidCardId(dto.getRfidCardId());
    if(!personName.isEmpty()) {
/*
      JSONObject jsonObject = new JSONObject();
      JSONObject jsonObjectPerson = new JSONObject();
      jsonObject.put("status", "success");
      jsonObject.put("person", jsonObjectPerson);
      jsonObjectPerson.put("name", registeredPerson.getName());
      jsonObjectPerson.put("rfid_card_id", registeredPerson.getRfidCardId());
*/
      System.out.println("Got person name: " + personName);
      return ResponseEntity.ok("success:" + "true," + "name:" + personName);
    }

//    JSONObject jsonObject = new JSONObject();
//    jsonObject.put("error", "The rfid card is not registered");
    System.out.println("Error! Could not verify rfid card!");
    return ResponseEntity.badRequest().body("success:" + "false." + "Invalid rfid card");
  }

  @PostMapping(value = "/verify_person_by_password")
  public ResponseEntity<String> getRegisteredPersonByPassword(@RequestBody PasswordDto dto) {
    String personName = "";
    personName = registeredPersonService.getRegisteredNamePersonByPassword(dto.getPassword());
    if(!personName.isEmpty()) {
/*
      JSONObject jsonObject = new JSONObject();
      JSONObject jsonObjectPerson = new JSONObject();
      jsonObject.put("status", "success");
      jsonObject.put("person", jsonObjectPerson);
      jsonObjectPerson.put("name", registeredPerson.getName());
      jsonObjectPerson.put("rfid_card_id", registeredPerson.getRfidCardId());
*/
      System.out.println("Got person name: " + personName);
      return ResponseEntity.ok("success:" + "true," + "name:" + personName);
    }

//    JSONObject jsonObject = new JSONObject();
//    jsonObject.put("error", "The password was not verified");
    System.out.println("Error! Could not verify password!");
    return ResponseEntity.badRequest().body("success:" + "false." + "Wrong password");
  }
}
