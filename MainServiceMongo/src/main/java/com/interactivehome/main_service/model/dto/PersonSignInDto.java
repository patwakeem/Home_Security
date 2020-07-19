package com.interactivehome.main_service.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PersonSignInDto {
  @JsonProperty("rfid_card_id")
  public String rfidCardId;
  @JsonProperty("rfid_card_id_signed")
  public Boolean rfidCardIdSigned;
}
