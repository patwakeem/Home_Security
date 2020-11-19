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
    private Integer _id;

    @Field("enabled")
    private Boolean enabled;

    @Field("description")
    private String description;

    @Field("updated_utc")
    private Date updatedUtc;

    @Field("created_utc")
    private Date createdUtc;

    public void updateAlarmFromDto(AlarmSystemDto dto) {
        enabled = dto.enabled;
        description = dto.description;
        updatedUtc = new Date(System.currentTimeMillis());
    }

    public void createAlarmFromDto(Integer id, AlarmSystemDto dto) {
        _id = id;
        enabled = dto.enabled;
        description = dto.description;
        createdUtc = new Date(System.currentTimeMillis());
    }
}
