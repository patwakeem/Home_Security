package com.interactivehome.main_service.model.events.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.interactivehome.main_service.model.events.dto.RegisteredPersonDto;

import java.util.Date;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Document(collection = "registered_person")
public class RegisteredPerson {
  @Id
  @Field("_id")
  private String id;

  private String name;

  @Field("rfid_card_id")
  private String rfidCardId;
  @Field("alarm_id")
  private Integer alarmId;

  private String password;
  @Field("updated_utc")
  private Date updatedUtc;

  public void mapFromDto(RegisteredPersonDto dto)
  {
    name = dto.getName();
    alarmId = dto.alarmId;
    rfidCardId = dto.getRfidCardId();
    password = dto.getPassword();
    updatedUtc = new Date(System.currentTimeMillis());
  }
}
