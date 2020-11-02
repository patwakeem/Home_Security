package com.interactivehome.main_service.model.device.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.interactivehome.main_service.model.device.dto.DoorSensorDto;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Document(collection = "door_sensor")
public class DoorSensor {
    @Id
    @Field("_id")
    private String id;

    @Field("sensor_id")
    private Integer sensorId;

    @Field("alarm_id")
    private Integer alarmId;

    @Field("enabled")
    private Boolean enabled;

    @Field("description")
    private String description;

    @Field("device_identifier")
    private String deviceIdentifier;

    @Field("battery_powered")
    private Boolean batteryPowered;

    @Field("arm_in")
    private Boolean armIn;

    @Field("arm_away")
    private Boolean armAway;

    @Field("created_utc")
    private Date createdUtc;

    public void mapFromDto(DoorSensorDto dto) {
        alarmId = dto.alarmId;
        sensorId = dto.sensorId;
        enabled = dto.enabled;
        description = dto.description;
        deviceIdentifier = dto.deviceIdentifier;
        batteryPowered = dto.batteryPowered;
        armIn = dto.armIn;
        armAway = dto.armAway;
        createdUtc = new Date(System.currentTimeMillis());
    }
}
