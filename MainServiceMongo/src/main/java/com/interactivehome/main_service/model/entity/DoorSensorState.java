package com.interactivehome.main_service.model.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.interactivehome.main_service.model.dto.DoorSensorStateDto;
import java.util.Date;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Document(collection = "door")
public class DoorSensorState {
    @Id
    @Field("_id")
    private String id;

    @Field("door_id")
    private Integer doorId;

    @Field("alarm_id")
    private Integer alarmId;

    @Field("door_state")
    private Boolean doorState;

    @Field("battery_voltage")
    private Float batteryVoltage;

    @Field("battery_percentage")
    private Integer batteryPercentage;

    @Field("updated_utc")
    private Date updatedUtc;

    public void mapFromDto(DoorSensorStateDto dto) {
        alarmId = dto.alarmId;
        doorId = dto.doorId;
        doorState = dto.doorState;
        batteryVoltage = dto.batteryVoltage;
        batteryPercentage = dto.batteryPercentage;
        updatedUtc = new Date(System.currentTimeMillis());
    }
}
