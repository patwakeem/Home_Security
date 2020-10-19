package com.interactivehome.main_service.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovementSensorStateDto {
    @JsonProperty("movement_sensor_id")
    public Integer movementSensorId;
    @JsonProperty("alarm_id")
    public Integer alarmId;
    @JsonProperty("movement_caught")
    public Boolean movementCaught;
    @JsonProperty("battery_voltage")
    public Float batteryVoltage;
    @JsonProperty("battery_percentage")
    public Integer batteryPercentage;
}
