package com.interactivehome.main_service.controller;


import com.interactivehome.main_service.config.AppProperties;
import com.interactivehome.main_service.model.dto.DoorSensorDto;
import com.interactivehome.main_service.model.entity.Alarm;
import com.interactivehome.main_service.model.entity.DoorSensor;
import com.interactivehome.main_service.service.AlarmService;
import com.interactivehome.main_service.service.DoorStateService;
import com.interactivehome.main_service.utils.CountdownTimer;
import io.micrometer.core.instrument.util.StringEscapeUtils;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class DoorStateController {

    private final DoorStateService doorStateService;

    @Autowired
    private AlarmService alarmService;

    @Autowired
    private AppProperties appProperties;

    @Value("$(verification_process_endpoint)")
    private String verificationProcessEndpoint;
    @Value("$(verification_process_timeout_sec)")
    private String verificationProcessTimeoutSec;

    private RestTemplate restTemplate;
    private Timer timer;

    public DoorStateController(DoorStateService doorStateService,
                                RestTemplate restTemplate) {
        this.doorStateService = doorStateService;
        this.restTemplate = restTemplate;
    }

    @PostMapping("/door")
    public ResponseEntity<String> postDoorState(@RequestBody DoorSensorDto doorSensorDto) {
        Alarm alarm = alarmService.getAlarmStateByAlarmId(1);
        System.out.println("Alarm state is : " + alarm.getAlarmState().toString());
        if(alarm.getAlarmState() == 0) {
            System.out.println("The alarm is deactivated.");
            return ResponseEntity.ok("200");
        }

        doorStateService.saveState(doorSensorDto);
        if(!doorSensorDto.getDoorState())
            return ResponseEntity.ok("201");

        // If the door opens and the alarm is armed then trigger the initiation of the verification process
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> responseEntity =
                restTemplate.exchange(
                    appProperties.getSecurityControllerEndpoint(), HttpMethod.GET, entity, String.class);

            if(responseEntity.getStatusCode() == HttpStatus.OK)
            {
                CountdownTimer countdownTimer = new CountdownTimer(restTemplate);
                countdownTimer.verificationTimerStart(appProperties.getVerificationProcessTimeoutSec());
                return ResponseEntity.ok("201");
            }
            else
            {
                System.out.println("Error response from security controller. " +
                    responseEntity.getStatusCode() + ": " + responseEntity.getBody());
            }

            return ResponseEntity.ok("201");
        }
        catch (RuntimeException exception) {
            System.out.println("Runtime exception: " + exception.getMessage());
            JSONObject payload = new JSONObject();
            try {
                payload.put("message", "The door opened but the verification process could not get started!");
                payload.put("error", StringEscapeUtils.escapeJson(exception.toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(payload.toString());
        }
    }

    @GetMapping("/door/{doorId}")
    public List<DoorSensor> getAllDoorStateByDoorIdFromDateToDate(
        @PathVariable Integer doorId,
        @RequestParam(value = "fromDate", defaultValue = "2020-07-01") @DateTimeFormat(pattern="yyyy-MM-dd") Date fromDate,
        @RequestParam(value = "toDate", defaultValue = "2020-07-14") @DateTimeFormat(pattern="yyyy-MM-dd") Date toDate)
    {
        return doorStateService.getDoorStateByDoorIdFromDateToDate(doorId, fromDate, toDate);
    }
}
