package com.interactivehome.main_service.model.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.interactivehome.main_service.model.dto.AlarmStateDto;
import java.util.Date;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Document(collection = "alarm")
public class Alarm {
  @Id
  @Field("_id")
  private String id;

  @Field("alarm_id")
  private Integer alarmId;

  private String name;

  @Field("default_alarm")
  private Boolean defaultAlarm;

  @Field("alarm_on")
  private Boolean alarmOn;

  @Field("alarm_state")
  private Integer alarmState;

  @Field("updated_utc")
  private Date updatedUtc;

  public void mapFromDto(AlarmStateDto dto) {
    alarmId = dto.alarmId;
    name = dto.name;
    defaultAlarm = dto.defaultAlarm;
    alarmOn = dto.alarmOn;
    alarmState = dto.alarmState;
    updatedUtc = new Date(System.currentTimeMillis());
  }
}
