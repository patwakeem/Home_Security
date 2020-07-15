package com.interactivehome.main_service.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TemperatureHumidiryGasDto {
    @JsonProperty("sensor_id")
    public Integer sensorId;
    public Float temperature;
    public Integer humidity;
    @JsonProperty("gas_value")
    public Integer gasValue;
}
