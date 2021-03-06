package com.interactivehome.main_service.model.events.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.interactivehome.main_service.model.events.dto.BatteryStateDto;
import java.util.Date;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Document(collection = "battery_state")
public class BatteryState {
  @Id
  @Field("_id")
  private Integer _id;

  @Field("alarm_id")
  private Integer alarmId;

  @Field("battery_voltage")
  private Float batteryVoltage;

  @Field("battery_percentage")
  private Integer batteryPercentage;

  @Field("updated_utc")
  private Date updatedUtc;

  public void mapFromDto(Integer id, BatteryStateDto dto) {
    _id = id;
    alarmId = dto.alarmId;
    batteryVoltage = dto.batteryVoltage;
    batteryPercentage = dto.batteryPercentage;
    updatedUtc = new Date(System.currentTimeMillis());
  }
}
