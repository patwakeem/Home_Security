package com.interactivehome.main_service.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MovementSensorDto {
    @JsonProperty("movement_sensor_id")
    private Integer movementSensorId;
}
