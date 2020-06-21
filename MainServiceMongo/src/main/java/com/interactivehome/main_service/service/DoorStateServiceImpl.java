package com.interactivehome.main_service.service;

import com.interactivehome.main_service.model.dto.DoorSensorDTO;
import com.interactivehome.main_service.model.entity.DoorSensor;
import com.interactivehome.main_service.repository.DoorStateRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
public class DoorStateServiceImpl implements DoorStateService {

    private final DoorStateRepository doorRepository;

    public DoorStateServiceImpl(DoorStateRepository doorStateRepository)
    {
        this.doorRepository = doorStateRepository;
    }

    @Override
    public void saveState(DoorSensorDTO dto) {
        DoorSensor doorSensor = new DoorSensor();
        doorSensor.mapFromDto(dto);
        doorRepository.save(doorSensor);
    }

    @Override
    public DoorSensorDTO getState(ObjectId id) {
        return null;
    }
}
