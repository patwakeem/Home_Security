package com.interactivehome.main_service.model.events.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonSignInDto {
  @JsonProperty("rfid_card_id")
  public String rfidCardId;
  @JsonProperty("alarm_id")
  public Integer alarmId;
  @JsonProperty("rfid_card_id_signed")
  public Boolean rfidCardIdSigned;
}
