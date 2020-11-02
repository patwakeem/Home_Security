package com.interactivehome.main_service.model.device.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.interactivehome.main_service.model.device.dto.AlarmSystemDto;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Document(collection = "alarm_system")
public class AlarmSystem {
    @Id
    @Field("_id")
    private String id;

    @Field("alarm_id")
    private Integer alarmId;

    @Field("enabled")
    private Boolean enabled;

    @Field("description")
    private String description;

    @Field("active_alarm")
    private Boolean activeAlarm;

    @Field("created_utc")
    private Date createdUtc;

    public void mapFromDto(AlarmSystemDto dto) {
        alarmId = dto.alarmId;
        enabled = dto.enabled;
        description = dto.description;
        activeAlarm = dto.activeAlarm;
        createdUtc = new Date(System.currentTimeMillis());
    }
}
