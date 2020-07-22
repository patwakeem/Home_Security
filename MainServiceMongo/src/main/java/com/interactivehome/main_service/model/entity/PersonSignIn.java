package com.interactivehome.main_service.model.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.interactivehome.main_service.model.dto.PersonSignInDto;
import java.util.Date;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Document(collection = "person_signin")
public class PersonSignIn {
  @Id
  @Field("_id")
  private String id;

  private String name;
  @Field("rfid_card_id")
  private String rfidCardId;
  @Field("rfid_card_id_signed")
  private Boolean rfidCardIdSigned;
  @Field("updated_utc")
  private Date updatedUtc;

  public void mapFromDto(PersonSignInDto dto) {
    rfidCardId = dto.rfidCardId;
    rfidCardIdSigned = dto.rfidCardIdSigned;
    updatedUtc = new Date(System.currentTimeMillis());
  }
}
