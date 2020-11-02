package com.interactivehome.main_service.model.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.interactivehome.main_service.model.dto.TemperatureHumidiryGasSensorStateDto;
import java.util.Date;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Document(collection = "temperature_humidity_gas_state")
public class TemperatureHumidityGasSensorState {
  @Id
  @Field("_id")
  private String id;
  @Field("sensor_id")
  private Integer sensorId;

  @Field("alarm_id")
  private Integer alarmId;

  private Float temperature;
  private Integer humidity;
  @Field("gas_value")
  private Integer gasValue;
  @Field("battery_voltage")
  private Float batteryVoltage;
  @Field("battery_percentage")
  private Integer batteryPercentage;
  @Field("updated_utc")
  private Date updatedUtc;

  public void mapFromDto(TemperatureHumidiryGasSensorStateDto dto) {
    alarmId = dto.alarmId;
    sensorId = dto.sensorId;
    temperature = dto.temperature;
    humidity = dto.humidity;
    gasValue = dto.gasValue;
    batteryVoltage = dto.batteryVoltage;
    batteryPercentage = dto.batteryPercentage;
    updatedUtc = new Date(System.currentTimeMillis());
  }
}
