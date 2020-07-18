package com.interactivehome.main_service.controller;

import com.interactivehome.main_service.model.entity.RegisteredPerson;
import com.interactivehome.main_service.service.RegisteredPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class VerifyPersonController {

  @Autowired
  private RegisteredPersonService registeredPersonService;

  @GetMapping(value = "/verify_person_by_rfid_card/{rfidCardId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getRegisteredPersonByRfidCardId(@PathVariable String rfidCardId) throws JSONException {
    RegisteredPerson registeredPerson = registeredPersonService.getRegisteredPersonByRfidCardId(rfidCardId);
    if(registeredPerson != null) {
      JSONObject jsonObject = new JSONObject();
      JSONObject jsonObjectPerson = new JSONObject();
      jsonObject.put("status", "success");
      jsonObject.put("person", jsonObjectPerson);
      jsonObjectPerson.put("name", registeredPerson.getName());
      jsonObjectPerson.put("rfid_card_id", registeredPerson.getRfidCardId());
      return ResponseEntity.ok(jsonObject.toString());
    }

    JSONObject jsonObject = new JSONObject();
    jsonObject.put("error", "The rfid card is not registered");
    return ResponseEntity.badRequest().body(jsonObject.toString());
  }

  @GetMapping(value = "/verify_person_by_password/{password}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getRegisteredPersonByPassword(@PathVariable String password) throws JSONException {
    RegisteredPerson registeredPerson = registeredPersonService.getRegisteredPersonByPassword(password);
    if(registeredPerson != null) {
      JSONObject jsonObject = new JSONObject();
      JSONObject jsonObjectPerson = new JSONObject();
      jsonObject.put("status", "success");
      jsonObject.put("person", jsonObjectPerson);
      jsonObjectPerson.put("name", registeredPerson.getName());
      jsonObjectPerson.put("rfid_card_id", registeredPerson.getRfidCardId());
      return ResponseEntity.ok(jsonObject.toString());
    }

    JSONObject jsonObject = new JSONObject();
    jsonObject.put("error", "The password was not verified");
    return ResponseEntity.badRequest().body(jsonObject.toString());
  }
}
