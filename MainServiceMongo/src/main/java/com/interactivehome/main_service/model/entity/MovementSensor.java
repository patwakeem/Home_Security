package com.interactivehome.main_service.model.entity;

import com.interactivehome.main_service.model.dto.MovementSensorDto;
import java.util.Date;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "movement_sensor")
public class MovementSensor {
  @Id
  @Field("_id")
  private String id;

  @Field("movement_sensor_id")
  private Integer movementSensorId;
  @Field("movement_sensor_state")
  private Boolean movementSensorState;
  @Field("movement_sensor_activated")
  private Boolean movementSensorActivated;
  @Field("updated_utc")
  private Date updatedUtc;

  public void mapFromDto(MovementSensorDto dto)
  {
    movementSensorId = dto.getMovementSensorId();
    movementSensorState = dto.getMovementSensorState();
    movementSensorActivated = dto.getMovementSensorActivated();
  }
}
