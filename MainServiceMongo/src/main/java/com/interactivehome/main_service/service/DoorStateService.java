package com.interactivehome.main_service.service;


import com.interactivehome.main_service.model.dto.DoorSensorDTO;
import org.bson.types.ObjectId;

public interface DoorStateService {
    void saveState(DoorSensorDTO dto);
    DoorSensorDTO getState(ObjectId id);
}
