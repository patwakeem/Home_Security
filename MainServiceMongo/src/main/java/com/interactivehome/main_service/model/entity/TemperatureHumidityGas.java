package com.interactivehome.main_service.model.entity;

import com.interactivehome.main_service.model.dto.TemperatureHumidiryGasDto;
import java.util.Date;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "temperature_humidity_gas")
public class TemperatureHumidityGas {
  @Id
  @Field("_id")
  private String id;
  @Field("sensor_id")
  private Integer sensorId;
  private Float temperature;
  private Integer humidity;
  @Field("gas_value")
  private Integer gasValue;
  @Field("updated_utc")
  private Date updatedUtc;

  public void mapFromDto(TemperatureHumidiryGasDto dto) {
    sensorId = dto.sensorId;
    temperature = dto.temperature;
    humidity = dto.humidity;
    gasValue = dto.gasValue;
    updatedUtc = new Date(System.currentTimeMillis());
  }
}
