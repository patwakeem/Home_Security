package com.interactivehome.main_service.model.device.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlarmSystemDto {
    @JsonProperty("_id")
    public Integer _id;
    public String description;
    public Boolean enabled;
}
