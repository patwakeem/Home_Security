package com.interactivehome.main_service.controller;

import com.interactivehome.main_service.model.dto.BatteryDto;
import com.interactivehome.main_service.service.BatteryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
