package com.interactivehome.main_service.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DoorSensorDTO {
    @JsonProperty("state")
    public Boolean doorState;
    @JsonProperty("updated_utc")
    public double updatedUtc;
}
