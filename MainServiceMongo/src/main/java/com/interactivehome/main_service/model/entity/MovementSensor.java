package com.interactivehome.main_service.model.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.interactivehome.main_service.model.dto.MovementSensorDto;
import java.util.Date;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Document(collection = "movement_sensor")
public class MovementSensor {
  @Id
  @Field("_id")
  private String id;

  @Field("movement_sensor_id")
  private Integer movementSensorId;
  @Field("updated_utc")
  private Date updatedUtc;

  public void mapFromDto(MovementSensorDto dto)
  {
    movementSensorId = dto.getMovementSensorId();
    updatedUtc = new Date(System.currentTimeMillis());
  }
}
