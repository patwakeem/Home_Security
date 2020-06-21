package com.interactivehome.main_service.model.entity;

import com.interactivehome.main_service.model.dto.DoorSensorDTO;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "door_state")
public class DoorSensor {
    @Id
    private ObjectId id;

    @Field("state")
    private Boolean state;

    @Field("updated_utc")
    private double updateUtc;

    public void mapFromDto(DoorSensorDTO dto) {
        state = dto.doorState;
        updateUtc = System.currentTimeMillis();
    }
}
