package com.interactivehome.main_service.controller;

import com.interactivehome.main_service.model.dto.*;
//import com.interactivehome.main_service.service.DoorStateService;
import com.interactivehome.main_service.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;


@RestController
public class MainController {

    private final MainService mainService;

//    @Autowired
//    private DoorStateService doorStateService;

    private RestTemplate restTemplate;
    private Timer timer;

    public MainController(MainService mainService,
//                          DoorStateService doorStateService,
                          RestTemplate restTemplate) {
        this.mainService = mainService;
//        this.doorStateService = doorStateService;
        this.restTemplate = restTemplate;
    }

    public enum timer_state {
        started,
        expired,
        stopped
    }

    private timer_state timerState;

    class TimerExpired extends TimerTask {
        public void run() {
            verificationTimerExpired();
        }
    }

    public void verificationTimerStart(int seconds) {
        if(timerState != timer_state.started) {
            timer = new Timer();
            timerState = timer_state.started;
            timer.schedule(new TimerExpired(), seconds * 1000);
        }
    }

    public void verificationTimerStop() {
        if(timerState == timer_state.started) {
            timerState = timer_state.stopped;
            timer.cancel();
        }
    }

    public void verificationTimerExpired() {
        if(timerState == timer_state.started) {
            timerState = timer_state.expired;
            timer.cancel();
            OnTimerExpired();
        }
    }

    @PostMapping("/alarm_state")
    public ResponseEntity<String> PostAlarmState(@RequestBody AlarmDTO alarmDTO) {
        mainService.setAlarmState(alarmDTO);
        return ResponseEntity.ok("200");
    }

/*    @PostMapping("/door")
    public ResponseEntity<String> PostDoorState(@RequestBody DoorSensorDTO doorSensorDTO) {
        if(mainService.getAlarmState().equals("0"))
            return ResponseEntity.ok("200");

        //mainService.setDoorState(doorSensorDTO);
        doorStateService.saveState(doorSensorDTO);
        if(!doorSensorDTO.getDoorState())
            return ResponseEntity.ok("200");

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        final String securityControllerInitiateVerificationProcess = "http://192.168.1.154:8080/initiate_verification_process";

        ResponseEntity<String> responseEntity =
                restTemplate.exchange(securityControllerInitiateVerificationProcess, HttpMethod.GET, entity, String.class);
        if(responseEntity.getStatusCode() == HttpStatus.OK)
        {
            verificationTimerStart(15);
            return ResponseEntity.ok("200");
        }
        else
        {
            // Log error
        }

        return ResponseEntity.ok("500");
    }
*/
    @PostMapping("/sensors_values")
    public ResponseEntity<String> PostSensorValues(@RequestBody SensorsDTO sensorsDTO) {
        mainService.setSensorsValues(sensorsDTO);
        return ResponseEntity.ok("200");
    }

    @PostMapping("/verify_person")
    public ResponseEntity<String> PostPersonVerified(@RequestBody PersonVerifiedDTO personVerifiedDTO) {
        mainService.setPersonVerified(personVerifiedDTO);
        if(personVerifiedDTO.getPerson_verified()) {
            verificationTimerStop();
            AlarmDTO alarmDTO = new AlarmDTO();
            alarmDTO.setAlarm_on(false);
            alarmDTO.setAlarm_state(0);
            mainService.setAlarmState(alarmDTO);
        }
        return ResponseEntity.ok("200");
    }

    @PostMapping("/verify_password")
    public ResponseEntity<String> PostVerifyPassword(@RequestBody VerifyPasswordDTO verifyPasswordDTO) {
        mainService.setVerifyPassword(verifyPasswordDTO);
        return ResponseEntity.ok("200");
    }

    @PostMapping("/alarm_off")
    public ResponseEntity<String> PostAlarmOff(@RequestBody AlarmDTO alarmDTO) {
        String alarmControllerTriggerAlarm = "http://192.168.1.129:8080/trigger_alarm";

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        alarmDTO.setAlarm_on(alarmDTO.getAlarm_on());
        HttpEntity<AlarmDTO> requestUpdate = new HttpEntity<>(alarmDTO, headers);

        ResponseEntity<Void> response =
                restTemplate.exchange(alarmControllerTriggerAlarm, HttpMethod.PUT, requestUpdate, Void.class);
        if(response.getStatusCode() != HttpStatus.OK) {
            // Log error
        }
        return ResponseEntity.ok("200");
    }

    public void OnTimerExpired()
    {
        String alarmControllerTriggerAlarm = "http://192.168.1.129:8080/trigger_alarm";

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        AlarmDTO alarmDTO = new AlarmDTO();
        alarmDTO.setAlarm_on(true);
        HttpEntity<AlarmDTO> requestUpdate = new HttpEntity<>(alarmDTO, headers);

        ResponseEntity<Void> response =
                restTemplate.exchange(alarmControllerTriggerAlarm, HttpMethod.PUT, requestUpdate, Void.class);
        if(response.getStatusCode() != HttpStatus.OK) {
            // Log error
        }
    }

    @GetMapping(value = "/temperature", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ThermometerSensorDTO> getTemperature() {
        String temperatureEntity = mainService.getTemperature();
        ThermometerSensorDTO dto = new ThermometerSensorDTO();
        dto.setTemperature(Float.parseFloat(temperatureEntity));
        return ResponseEntity.ok(dto);
    }
}
