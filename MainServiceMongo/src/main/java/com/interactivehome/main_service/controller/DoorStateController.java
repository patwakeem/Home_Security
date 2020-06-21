package com.interactivehome.main_service.controller;


import com.interactivehome.main_service.model.dto.DoorSensorDTO;
import com.interactivehome.main_service.service.DoorStateService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Timer;

@RestController
public class DoorStateController {

    private final DoorStateService doorStateService;

    private RestTemplate restTemplate;
    private Timer timer;

    public DoorStateController(DoorStateService doorStateService,
                          RestTemplate restTemplate) {
        this.doorStateService = doorStateService;
        this.restTemplate = restTemplate;
    }

    @PostMapping("/door")
    public ResponseEntity<String> PostDoorState(@RequestBody DoorSensorDTO doorSensorDTO) {
//        if(mainService.getAlarmState().equals("0"))
//            return ResponseEntity.ok("200");

        doorStateService.saveState(doorSensorDTO);
        return ResponseEntity.ok("200");
/*        if(!doorSensorDTO.getDoorState())
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
*/    }


}
