package com.interactivehome.main_service.utils;

import com.interactivehome.main_service.model.events.dto.AlarmSystemStateDto;
import com.interactivehome.main_service.service.events.AlarmSystemStateService;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CountdownTimer extends Timer
{
    @Autowired
    private AlarmSystemStateService alarmSystemStateService;

    @Autowired
    private RestTemplate restTemplate;

    private enum timer_state {
        started,
        expired,
        stopped
    }

    private timer_state timerState;
    private Integer alarmId;

    class TimerExpired extends TimerTask {
        public void run() {
            verificationTimerExpired();
        }
    }

    public void verificationTimerStart(Integer id, int seconds) {
        alarmId = id;
        schedule(new TimerExpired(),seconds * 1000);
        timerState = timer_state.started;
        System.out.println("The verification timer is initiated. Timeout = " + seconds + " sec.");
    }

    public void verificationTimerStop() {
        if(timerState == timer_state.started) {
            this.cancel();
            timerState = timer_state.stopped;
            System.out.println("The countdown timer is forced to stop!");
        }
    }

    private void verificationTimerExpired() {
        this.cancel();
        timerState = timer_state.expired;
        System.out.println("The verification timer has expired without any person been verified!");
        OnTimerExpired();
    }

    private void OnTimerExpired() {
        System.out.println("OnTimerExpired is called.");
        try {
            String alarmControllerTriggerAlarm = "http://192.168.1.129:8080/trigger_alarm";

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            AlarmSystemStateDto alarmSystemStateDTO = new AlarmSystemStateDto();
            alarmSystemStateDTO.set_id(alarmId);
            alarmSystemStateDTO.setAlarmState(2);
            alarmSystemStateDTO.setAlarmOn(true);
            alarmSystemStateService.saveAlarmStateById(alarmId, alarmSystemStateDTO);

            HttpEntity<AlarmSystemStateDto> requestUpdate = new HttpEntity<>(alarmSystemStateDTO, headers);

            ResponseEntity<Void> response =
                restTemplate.exchange(alarmControllerTriggerAlarm, HttpMethod.POST, requestUpdate, Void.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                System.out.println("Error response from security controller. " +
                    response.getStatusCode() + ": " + requestUpdate.getBody());
            }
        }
        catch (RuntimeException exception) {
            System.out.println("Runtime exception: " + exception.getMessage());
        }
    }
}
