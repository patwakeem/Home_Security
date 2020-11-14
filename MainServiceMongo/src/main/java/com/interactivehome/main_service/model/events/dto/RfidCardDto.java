package com.interactivehome.main_service.model.events.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RfidCardDto {
  @JsonProperty("rfid_card_id")
  public String rfidCardId;
  @JsonProperty("alarm_id")
  public Integer alarmId;
}
