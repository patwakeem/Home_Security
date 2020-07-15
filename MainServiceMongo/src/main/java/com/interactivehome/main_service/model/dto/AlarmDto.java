package com.interactivehome.main_service.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlarmDto {
    @JsonProperty("alarm_id")
    public Integer alarmId;
    @JsonProperty("alarm_on")
    public Boolean alarmOn;
    @JsonProperty("alarm_state")
    public Integer alarmState;
}
