package com.interactivehome.main_service.controller;

import com.interactivehome.main_service.model.dto.AlarmDto;
import com.interactivehome.main_service.model.entity.Alarm;
import com.interactivehome.main_service.service.AlarmService;
import java.util.Date;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AlarmController {
  private final AlarmService alarmService;

  public AlarmController(AlarmService alarmService) {
    this.alarmService = alarmService;
  }

  @PostMapping("/alarm")
  public ResponseEntity<String> postAlarmState(@RequestBody AlarmDto dto) {
    alarmService.saveAlarmState(dto);
    return ResponseEntity.ok("201");
  }

  @GetMapping(value = "/alarm/{alarmId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Integer getAlarmStateByAlarmId(@PathVariable Integer alarmId) {
    Alarm alarm = alarmService.getAlarmStateByAlarmId(alarmId);
    return alarm.getAlarmState();
  }

  @GetMapping("/alarm/{alarmId}")
  public List<Alarm> getAllAlarmByAlarmIdFromDateToDate(
      @PathVariable Integer sensorId,
      @RequestParam(value = "fromDate", defaultValue = "2020-07-01") @DateTimeFormat(pattern="yyyy-MM-dd") Date fromDate,
      @RequestParam(value = "toDate", defaultValue = "2020-07-14") @DateTimeFormat(pattern="yyyy-MM-dd") Date toDate)
  {
    return alarmService.getAlarmStateByAlarmIdFromDateToDate(sensorId, fromDate, toDate);
  }
}
