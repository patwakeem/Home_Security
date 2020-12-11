package com.interactivehome.main_service.model.events.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlarmSystemStateDto {
    @JsonProperty("_id")
    public Integer _id;
    @JsonProperty("verification_activated")
    public Boolean verificationActivated;
    @JsonProperty("alarm_on")
    public Boolean alarmOn;
    @JsonProperty("alarm_state")
    public Integer alarmState;
}
