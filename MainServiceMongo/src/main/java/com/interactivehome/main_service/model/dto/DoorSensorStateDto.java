package com.interactivehome.main_service.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DoorSensorStateDto {
    @JsonProperty("door_id")
    public Integer doorId;
    @JsonProperty("alarm_id")
    public Integer alarmId;
    @JsonProperty("door_state")
    public Boolean doorState;
    @JsonProperty("battery_voltage")
    public Float batteryVoltage;
    @JsonProperty("battery_percentage")
    public Integer batteryPercentage;
}
