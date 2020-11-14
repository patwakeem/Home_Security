package com.interactivehome.main_service.controller.events;


import com.interactivehome.main_service.config.AppProperties;
import com.interactivehome.main_service.model.events.dto.DoorSensorStateDto;
import com.interactivehome.main_service.model.events.entity.DoorSensorState;
import com.interactivehome.main_service.service.events.AlarmService;
import com.interactivehome.main_service.service.events.DoorSensorStateService;
import com.interactivehome.main_service.utils.CountdownTimer;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@CrossOrigin(origins = "*")
@RestController
public class DoorStateController {

    private final DoorSensorStateService doorSensorStateService;

    @Autowired
    private AlarmService alarmService;

    @Autowired
    private AppProperties appProperties;

    @Autowired
    CountdownTimer countdownTimer;

    @Value("$(securityControllerIpPort)")
    private String securityControllerIpPort;
    @Value("$(verificationProcessEndpoint)")
    private String verificationProcessEndpoint;
    @Value("$(verification_process_timeout_sec)")
    private String verificationProcessTimeoutSec;

    private RestTemplate restTemplate;

    public DoorStateController(DoorSensorStateService doorSensorStateService,
                               RestTemplate restTemplate) {
        this.doorSensorStateService = doorSensorStateService;
        this.restTemplate = restTemplate;
    }


    @PostMapping("/door")
    public ResponseEntity<String> postDoorState(@RequestBody DoorSensorStateDto doorSensorStateDto) {

        Integer alarmState = alarmService.getAlarmStateByAlarmId(doorSensorStateDto.alarmId);
        if(alarmState == null)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("The alarm is not set");
        System.out.println("Alarm state is : " + alarmState.toString());
        if(alarmState == 0) {
            System.out.println("The alarm is deactivated.");
            return ResponseEntity.ok("200");
        }

        doorSensorStateService.saveState(doorSensorStateDto);
        if(!doorSensorStateDto.getDoorState())
            return ResponseEntity.ok("201");

        // If the door opens and the alarm is armed then trigger the initiation of the verification process
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            System.out.println("Security controller verification process endpoint: " +
                appProperties.getSecurityControllerIpPort() + appProperties.getVerificationProcessEndpoint());
            ResponseEntity<String> responseEntity =
                restTemplate.exchange(
                    appProperties.getSecurityControllerIpPort()
                        + appProperties.getVerificationProcessEndpoint(),
                    HttpMethod.GET,
                    entity,
                    String.class);

            if(responseEntity.getStatusCode() == HttpStatus.OK)
            {
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

    @PutMapping("/doorbattery")
    public ResponseEntity<String> postDoorBatteryVoltage(@RequestBody DoorSensorStateDto doorSensorStateDto) {

        doorSensorStateService.saveBatteryVoltage(doorSensorStateDto);

        return ResponseEntity.ok("200");
    }

    @GetMapping("/door/{alarmId}/{sensorId}")
    public List<DoorSensorState> getDoorStateByAlarmIdAndSensorIdFromDateToDate(
        @PathVariable Integer alarmId,
        @PathVariable Integer sensorId,
        @RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fromDate,
        @RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date toDate)
    {
        return doorSensorStateService.getDoorStateByAlarmIdAndSensorIdFromDateToDate(alarmId, sensorId, fromDate, toDate);
    }
}
