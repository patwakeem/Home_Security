package com.interactivehome.main_service.controller;

import com.interactivehome.main_service.model.dto.TemperatureHumidiryGasDto;
import com.interactivehome.main_service.model.entity.TemperatureHumidityGas;
import com.interactivehome.main_service.service.TemperatureHumidityGasService;
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
  private final TemperatureHumidityGasService temperatureHumidityGasService;

  public TemperatureHumidityGasController(TemperatureHumidityGasService temperatureHumidityGasService) {
    this.temperatureHumidityGasService = temperatureHumidityGasService;
  }

  @PostMapping("/temperature_humidity_gas")
  public ResponseEntity<String> postValues(@RequestBody TemperatureHumidiryGasDto dto) {
    temperatureHumidityGasService.saveValues(dto);
    return ResponseEntity.ok("201");
  }
  
  @GetMapping("/temperature_humidity_gas/{sensorId}")
  public List<TemperatureHumidityGas> getAllTemperatureHumidityGasBySensorIdFromDateToDate(
      @PathVariable Integer sensorId,
      @RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fromDate,
      @RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date toDate)
  {
    return temperatureHumidityGasService.getValuesBySensorIdFromDateToDate(sensorId, fromDate, toDate);
  }
}
