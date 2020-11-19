package com.interactivehome.main_service.repository.device;

import com.interactivehome.main_service.model.device.entity.MotionSensor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotionSensorRepository extends MongoRepository<MotionSensor, String> {
}
