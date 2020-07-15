package com.interactivehome.main_service.model.entity;

import com.interactivehome.main_service.model.dto.RegisteredPersonDto;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "registered_person")
public class RegisteredPerson {
  @Id
  @Field("_id")
  private String id;

  private String name;
  @Field("rfid_card_id")
  private String rfidCardId;
  private String password;

  public void mapFromDto(RegisteredPersonDto dto)
  {
    name = dto.getName();
    rfidCardId = dto.getRfidCardId();
    password = dto.getPassword();
  }
}
