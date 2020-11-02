package com.interactivehome.main_service.model.device.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.interactivehome.main_service.model.device.dto.MovementSensorDto;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Document(collection = "movement_sensor")
public class MovementSensor {
    @Id
    @Field("_id")
    private String id;

    @Field("sensor_id")
    private Integer sensorId;

    @Field("alarm_id")
    private Integer alarmId;

    @Field("description")
    private String description;
    @Field("device_identifier")
    private String deviceIdentifier; // i.e. "Living room 1" or "Kids bedroom 1"
    @Field("enabled")
    private Boolean enabled;
    @Field("battery_powered")
    private Boolean batteryPowered;
    @Field("arm_in")
    private Boolean armIn;
    @Field("arm_away")
    private Boolean armAway;
    @Field("created_utc")
    private Date createdUtc;

    public void mapFromDto(MovementSensorDto dto)
    {
        alarmId = dto.alarmId;
        sensorId = dto.sensorId;
        description = dto.description;
        deviceIdentifier = dto.deviceIdentifier;
        enabled = dto.enabled;
        batteryPowered = dto.batteryPowered;
        armIn = dto.armIn;
        armAway = dto.armAway;
        createdUtc = new Date(System.currentTimeMillis());
    }
}
