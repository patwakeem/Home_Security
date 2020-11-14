package com.interactivehome.main_service.controller.events;

import com.interactivehome.main_service.model.events.dto.BatteryDto;
import com.interactivehome.main_service.model.events.entity.Battery;
import com.interactivehome.main_service.service.events.BatteryService;
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
public class BatteryController {
  private final BatteryService batteryService;

  public BatteryController(BatteryService batteryService) {
    this.batteryService = batteryService;
  }

  @PostMapping("/battery")
  public ResponseEntity<String> postBatteryVoltage(@RequestBody BatteryDto batteryDto) {
    batteryService.saveVoltage(batteryDto);
    return ResponseEntity.ok("201");
  }

  @GetMapping("/battery/{batteryId}")
  public List<Battery> getBatteryVoltageByBatteryIdFromDateToDate(
      @PathVariable Integer batteryId,
      @RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fromDate,
      @RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date toDate)
  {
    return batteryService.getVoltageByBatteryIdFromDateToDate(batteryId, fromDate, toDate);
  }
}
