package com.interactivehome.main_service.model.dto;

import lombok.Data;
/*
enum alarm_state_ {
    alarm_initialized,
    alarm_fully_armed,
    alarm_door_only_armed,
    alarm_disarmed
};
*/
@Data
public class AlarmDTO {
    private Boolean alarm_on;
    //alarm_state_ alarm_state;
    private Integer alarm_state;
}
