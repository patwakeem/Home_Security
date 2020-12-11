package com.interactivehome.main_service.controller.events;

import com.interactivehome.main_service.model.events.dto.EnvironmentSensorStateDto;
import com.interactivehome.main_service.model.events.entity.EnvironmentSensorState;
import com.interactivehome.main_service.service.events.EnvironmentSensorStateService;
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
public class EnvironmentSensorStateController {
  private final EnvironmentSensorStateService environmentSensorStateService;

  public EnvironmentSensorStateController(EnvironmentSensorStateService environmentSensorStateService) {
    this.environmentSensorStateService = environmentSensorStateService;
  }

  @PostMapping("/environment_sensor_state/{id}")
  public ResponseEntity<String> postValues(@PathVariable Integer id,
                                           @RequestBody EnvironmentSensorStateDto dto) {
    environmentSensorStateService.saveValuesBySensorId(id, dto);
    return ResponseEntity.ok("201");
  }
  
  @GetMapping("/environment_sensor_state/{id}")
  public List<EnvironmentSensorState> getAllTemperatureHumidityGasByAlarmIdAndSensorIdFromDateToDate(
      @PathVariable Integer id,
      @RequestParam(value = "alarmId") Integer alarmId,
      @RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fromDate,
      @RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date toDate)
  {
    return environmentSensorStateService.getValuesByAlarmIdAndSensorIdFromDateToDate(alarmId, id, fromDate, toDate);
  }
}
