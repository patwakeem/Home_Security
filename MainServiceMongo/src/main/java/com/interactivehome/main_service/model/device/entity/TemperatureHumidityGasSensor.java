package com.interactivehome.main_service.model.device.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.interactivehome.main_service.model.device.dto.TemperatureHumidityGasSensorDto;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Document(collection = "temperature_humidity_gas")
public class TemperatureHumidityGasSensor {
    @Id
    @Field("_id")
    private String id;
    @Field("sensor_id")
    private Integer sensorId;

    @Field("alarm_id")
    private Integer alarmId;

    private String description;
    @Field("device_identifier")
    private String deviceIdentifier; // i.e. "Living room 1" or "Kids bedroom 1"

    @Field("battery_powered")
    private Boolean batteryPowered;
    @Field("enabled")
    private Boolean enabled;
    @Field("created_utc")
    private Date createdUtc;

    public void mapFromDto(TemperatureHumidityGasSensorDto dto) {
        alarmId = dto.alarmId;
        sensorId = dto.sensorId;
        enabled = dto.enabled;
        description = dto.description;
        deviceIdentifier = dto.deviceIdentifier;
        batteryPowered = dto.batteryPowered;
        createdUtc = new Date(System.currentTimeMillis());
    }
}
