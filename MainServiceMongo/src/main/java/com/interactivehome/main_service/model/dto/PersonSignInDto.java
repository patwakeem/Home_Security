package com.interactivehome.main_service.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonSignInDto {
  @JsonProperty("rfid_card_id")
  public String rfidCardId;
  @JsonProperty("rfid_card_id_signed")
  public Boolean rfidCardIdSigned;
}
