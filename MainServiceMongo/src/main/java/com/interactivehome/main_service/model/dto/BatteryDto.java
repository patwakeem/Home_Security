package com.interactivehome.main_service.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BatteryDto {
  @JsonProperty("battery_id")
  public Integer batteryId;
  @JsonProperty("battery_voltage")
  public Float batteryVoltage;
  @JsonProperty("battery_percentage")
  public Integer batteryPercentage;
}
