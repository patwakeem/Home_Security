package com.interactivehome.main_service.repository.device;

import com.interactivehome.main_service.model.device.entity.DoorSensor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoorSensorRepository extends MongoRepository<String, DoorSensor> {
}
