package com.interactivehome.main_service.controller.events;

import com.interactivehome.main_service.model.events.dto.MovementSensorStateDto;
import com.interactivehome.main_service.model.events.entity.MovementSensorState;
import com.interactivehome.main_service.service.events.AlarmService;
import com.interactivehome.main_service.service.events.MovementSensorStateService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@CrossOrigin(origins = "*")
@RestController
public class MovementSensorStateController {

  private final MovementSensorStateService movementSensorStateService;

  @Autowired
  private AlarmService alarmService;

  private final RestTemplate restTemplate;

  MovementSensorStateController(MovementSensorStateService movementSensorStateService,
                                RestTemplate restTemplate) {
    this.movementSensorStateService = movementSensorStateService;
    this.restTemplate = restTemplate;
  }

  @PostMapping("/movement_sensor")
  public ResponseEntity<String> postMovementSensorState(@RequestBody MovementSensorStateDto movementSensorStateDto)
  {
    Integer alarmState = alarmService.getAlarmStateByAlarmId(movementSensorStateDto.alarmId);
    System.out.println("Alarm state is : " + alarmState.toString());
    if(alarmState == 0) {
      System.out.println("The alarm is deactivated.");
      return ResponseEntity.ok("200");
    }

    movementSensorStateService.saveState(movementSensorStateDto);
    return ResponseEntity.ok("201");
  }

  @GetMapping("movement_sensor_state/{alarmId}/{sensorId}")
  public List<MovementSensorState> getSensorActivityByAlarmIdAndSensorIdFromDateToDate(
      @PathVariable Integer alarmId,
      @PathVariable Integer sensorId,
      @RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fromDate,
      @RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date toDate) {

    return movementSensorStateService.getSensorActivityByAlarmIdAndSensorId(alarmId, sensorId, fromDate, toDate);
  }
}
