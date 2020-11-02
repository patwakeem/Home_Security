package com.interactivehome.main_service.model.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.interactivehome.main_service.model.dto.MovementSensorStateDto;
import java.util.Date;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Document(collection = "movement_sensor_state")
public class MovementSensorState {
  @Id
  @Field("_id")
  private String id;

  @Field("sensor_id")
  private Integer sensorId;

  @Field("alarm_id")
  private Integer alarmId;

  @Field("movement_caught")
  private Boolean movementCaught;

  @Field("battery_voltage")
  private Float batteryVoltage;
  @Field("battery_percentage")
  private Integer batteryPercentage;

  @Field("updated_utc")
  private Date updatedUtc;

  public void mapFromDto(MovementSensorStateDto dto)
  {
    alarmId = dto.alarmId;
    sensorId = dto.sensorId;
    movementCaught = dto.movementCaught;
    batteryVoltage = dto.batteryVoltage;
    batteryPercentage = dto.batteryPercentage;
    updatedUtc = new Date(System.currentTimeMillis());
  }
}
