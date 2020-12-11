package com.interactivehome.main_service.model.events.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.interactivehome.main_service.model.events.dto.MotionSensorStateDto;

import java.util.Date;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Document(collection = "motion_sensor_state")
public class MotionSensorState {
  @Id
  @Field("_id")
  private Integer _id;

  @Field("alarm_id")
  private Integer alarmId;

  @Field("motion_caught")
  private Boolean motionCaught;

  @Field("updated_utc")
  private Date updatedUtc;

  public void mapFromDto(Integer id, MotionSensorStateDto dto)
  {
    _id = id;
    alarmId = dto.alarmId;
    motionCaught = dto.motionCaught;
    updatedUtc = new Date(System.currentTimeMillis());
  }
}
