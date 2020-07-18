package com.interactivehome.main_service.controller;

import com.interactivehome.main_service.model.dto.MovementSensorDto;
import com.interactivehome.main_service.model.entity.Alarm;
import com.interactivehome.main_service.service.AlarmService;
import com.interactivehome.main_service.service.MovementSensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@CrossOrigin(origins = "*")
@RestController
public class MovementSensorController {

  private final MovementSensorService movementSensorService;

  @Autowired
  private AlarmService alarmService;

  private final RestTemplate restTemplate;

  MovementSensorController(MovementSensorService movementSensorService,
                            RestTemplate restTemplate) {
    this.movementSensorService = movementSensorService;
    this.restTemplate = restTemplate;
  }

  @PostMapping("/movement_sensor")
  public ResponseEntity<String> postMovementSensorState(@RequestBody MovementSensorDto movementSensorDto)
  {
    Alarm alarm = alarmService.getAlarmStateByAlarmId(1);
    System.out.println("Alarm state is : " + alarm.getAlarmState().toString());
    if(alarm.getAlarmState() == 0) {
      System.out.println("The alarm is deactivated.");
      return ResponseEntity.ok("200");
    }

    movementSensorService.saveState(movementSensorDto);
    if(!movementSensorDto.getMovementSensorState())
      return ResponseEntity.ok("201");

    // If a movement is caught and the alarm is armed, then buzz the alarm
    if(movementSensorDto.getMovementSensorActivated())
      movementSensorService.buzzAlarm();

    return ResponseEntity.ok("201");
  }
}
