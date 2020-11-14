package com.interactivehome.main_service.controller.events;

import com.interactivehome.main_service.model.events.dto.TemperatureHumidiryGasSensorStateDto;
import com.interactivehome.main_service.model.events.entity.TemperatureHumidityGasSensorState;
import com.interactivehome.main_service.service.events.TemperatureHumidityGasSensorStateService;
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
public class TemperatureHumidityGasController {
  private final TemperatureHumidityGasSensorStateService temperatureHumidityGasSensorStateService;

  public TemperatureHumidityGasController(TemperatureHumidityGasSensorStateService temperatureHumidityGasSensorStateService) {
    this.temperatureHumidityGasSensorStateService = temperatureHumidityGasSensorStateService;
  }

  @PostMapping("/temperature_humidity_gas_state")
  public ResponseEntity<String> postValues(@RequestBody TemperatureHumidiryGasSensorStateDto dto) {
    temperatureHumidityGasSensorStateService.saveValues(dto);
    return ResponseEntity.ok("201");
  }
  
  @GetMapping("/temperature_humidity_gas_state/{alarmId}/{sensorId}")
  public List<TemperatureHumidityGasSensorState> getAllTemperatureHumidityGasByAlarmIdAndSensorIdFromDateToDate(
      @PathVariable Integer alarmId,
      @PathVariable Integer sensorId,
      @RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fromDate,
      @RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date toDate)
  {
    return temperatureHumidityGasSensorStateService.getValuesByAlarmIdAndSensorIdFromDateToDate(alarmId, sensorId, fromDate, toDate);
  }
}
