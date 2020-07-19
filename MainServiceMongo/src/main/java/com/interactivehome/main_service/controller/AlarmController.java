package com.interactivehome.main_service.controller;

import com.interactivehome.main_service.config.AppProperties;
import com.interactivehome.main_service.model.dto.AlarmDto;
import com.interactivehome.main_service.model.entity.Alarm;
import com.interactivehome.main_service.service.AlarmService;
import io.micrometer.core.instrument.util.StringEscapeUtils;
import java.util.Date;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
public class AlarmController {
  private final AlarmService alarmService;
  private final RestTemplate restTemplate;

  @Autowired
  private AppProperties appProperties;

  @Value("$(securityControllerIpPort)")
  private String securityControllerIpPort;
  @Value("$(alarmStateEndpoint)")
  private String alarmStateEndpoint;
  @Value("$(stopAlarmEndpoint)")
  private String stopAlarmEndpoint;

  public AlarmController(AlarmService alarmService,
                          RestTemplate restTemplate) {
    this.alarmService = alarmService;
    this.restTemplate = restTemplate;
  }

  @PostMapping("/alarm")
  public ResponseEntity postAlarmState(@RequestBody AlarmDto dto) {
    alarmService.saveAlarmState(dto);

    try {
      HttpHeaders requestHeaders = new HttpHeaders();
      requestHeaders.setContentType(MediaType.APPLICATION_JSON);
      JSONObject jsonObject = new JSONObject();
      try {
        jsonObject.put("alarm_state", dto.getAlarmState());
      }
      catch (JSONException e) {
        System.out.println(e.toString());
      }
      HttpEntity<JSONObject> requestEntity = new HttpEntity<>(jsonObject, requestHeaders);
      ResponseEntity<String> responseEntity =
          restTemplate.exchange(
              appProperties.getSecurityControllerIpPort() + appProperties.getAlarmStateEndpoint(),
              HttpMethod.POST,
              requestEntity,
              String.class);

      if(responseEntity.getStatusCode() == HttpStatus.OK)
      {
        System.out.println("The alarm state in the security controller has been updated successfully");
        return ResponseEntity.ok("200");
      }
      else
      {
        System.out.println("Error response from security controller while trying to update the alarm state. " +
            responseEntity.getStatusCode() + ": " + responseEntity.getBody());
        return responseEntity;
      }
    }
    catch (RuntimeException exception) {
      System.out.println("Runtime exception: " + exception.toString());
      JSONObject payload = new JSONObject();
      try {
        payload.put("message", "The security controller alarm state could not get updated!");
        payload.put("error", StringEscapeUtils.escapeJson(exception.toString()));
      } catch (JSONException e) {
        System.out.println(e.toString());
      }
      return new ResponseEntity<>(payload, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/alarm/{alarmId}")
  public List<Alarm> getAlarmByAlarmIdFromDateToDate(
      @PathVariable Integer alarmId,
      @RequestParam(value = "fromDate", required = false, defaultValue = "2020-07-01") @DateTimeFormat(pattern="yyyy-MM-dd") Date fromDate,
      @RequestParam(value = "toDate", required = false, defaultValue = "2020-07-14") @DateTimeFormat(pattern="yyyy-MM-dd") Date toDate)
  {
    return alarmService.getAlarmStateByAlarmIdFromDateToDate(alarmId, fromDate, toDate);
  }

  @PostMapping("/stop_alarm/{alarmId}")
  public ResponseEntity postStopAlarm(@RequestBody AlarmDto dto) {
    alarmService.stopAlarm(dto);

    try {
      HttpHeaders requestHeaders = new HttpHeaders();
      requestHeaders.setContentType(MediaType.APPLICATION_JSON);
      HttpEntity<String> requestEntity = new HttpEntity<>(requestHeaders);
      ResponseEntity<String> responseEntity =
          restTemplate.exchange(
              appProperties.getSecurityControllerIpPort() + appProperties.getAlarmStateEndpoint(),
              HttpMethod.GET,
              requestEntity,
              String.class);

      if(responseEntity.getStatusCode() == HttpStatus.OK)
      {
        System.out.println("The alarm stopped successfully in the security controller");
        return ResponseEntity.ok("200");
      }
      else
      {
        System.out.println("Error response from security controller while trying to update the alarm state. " +
            responseEntity.getStatusCode() + ": " + responseEntity.getBody());
        return responseEntity;
      }
    }
    catch (RuntimeException exception) {
      System.out.println("Runtime exception: " + exception.getMessage());
      JSONObject payload = new JSONObject();
      try {
        payload.put("message", "The security controller alarm state could not get updated!");
        payload.put("error", StringEscapeUtils.escapeJson(exception.toString()));
      } catch (JSONException e) {
        System.out.println(e.toString());
      }
      return new ResponseEntity<>(payload, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
