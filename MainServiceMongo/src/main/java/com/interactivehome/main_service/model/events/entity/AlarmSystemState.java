package com.interactivehome.main_service.model.events.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.interactivehome.main_service.model.events.dto.AlarmSystemStateDto;
import java.util.Date;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Document(collection = "alarm_system_state")
public class AlarmSystemState {
  @Id
  @Field("_id")
  private Integer _id;

  @Field("verification_activated")
  private Boolean verificationActivated;
  @Field("alarm_on")
  private Boolean alarmOn;

  @Field("alarm_state")
  private Integer alarmState;

  @Field("updated_utc")
  private Date updatedUtc;

  public void mapFromDto(Integer id, AlarmSystemStateDto dto) {
    _id = id;
    verificationActivated = dto.verificationActivated;
    alarmOn = dto.alarmOn;
    alarmState = dto.alarmState;
    updatedUtc = new Date(System.currentTimeMillis());
  }
}
