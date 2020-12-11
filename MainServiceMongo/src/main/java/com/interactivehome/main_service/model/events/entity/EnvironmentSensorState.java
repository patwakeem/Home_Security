package com.interactivehome.main_service.model.events.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.interactivehome.main_service.model.events.dto.EnvironmentSensorStateDto;

import java.rmi.activation.ActivationGroup_Stub;
import java.util.Date;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Document(collection = "environment_sensor_state")
public class EnvironmentSensorState {
  @Id
  @Field("_id")
  private Integer _id;

  @Field("alarm_id")
  private Integer alarmId;

  private Float temperature;
  private Float humidity;
  private Float pressure;
  private Float altitude;
  @Field("gas_value")
  private Integer gasValue;
  @Field("updated_utc")
  private Date updatedUtc;

  public void mapFromDto(Integer id, EnvironmentSensorStateDto dto) {
    _id = id;
    alarmId = dto.alarmId;
    temperature = dto.temperature;
    humidity = dto.humidity;
    pressure = dto.pressure;
    altitude = dto.altitude;
    gasValue = dto.gasValue;
    updatedUtc = new Date(System.currentTimeMillis());
  }
}
