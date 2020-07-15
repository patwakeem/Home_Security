package com.interactivehome.main_service.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DoorSensorDto {
    @JsonProperty("door_id")
    public Integer doorId;
    @JsonProperty("door_state")
    public Boolean doorState;
}
