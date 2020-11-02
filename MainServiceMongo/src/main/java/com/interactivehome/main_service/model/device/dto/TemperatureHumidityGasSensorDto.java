package com.interactivehome.main_service.model.device.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TemperatureHumidityGasSensorDto {
    @JsonProperty("sensor_id")
    public Integer sensorId;
    @JsonProperty("alarm_id")
    public Integer alarmId;
    public String description;
    @JsonProperty("device_identifier")
    public String deviceIdentifier; // i.e. "Living room 1" or "Kids bedroom 1"
    @JsonProperty("battery_powered")
    public Boolean batteryPowered;
    public Boolean enabled;
    @JsonProperty("arm_in")
    public Boolean armIn;
    @JsonProperty("arm_away")
    public Boolean armAway;
}
