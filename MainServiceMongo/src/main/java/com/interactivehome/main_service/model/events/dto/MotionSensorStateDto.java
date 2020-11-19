package com.interactivehome.main_service.model.events.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MotionSensorStateDto {
    @JsonProperty("sensor_id")
    public Integer sensorId;
    @JsonProperty("alarm_id")
    public Integer alarmId;
    @JsonProperty("motion_caught")
    public Boolean motionCaught;
    @JsonProperty("battery_voltage")
    public Float batteryVoltage;
    @JsonProperty("battery_percentage")
    public Integer batteryPercentage;
}
