package com.interactivehome.main_service.repository.events;

import com.interactivehome.main_service.model.events.entity.MotionSensorState;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotionSensorStateRepository extends MongoRepository<MotionSensorState, String> {

}
