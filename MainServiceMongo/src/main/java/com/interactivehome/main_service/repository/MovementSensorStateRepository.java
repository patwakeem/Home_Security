package com.interactivehome.main_service.repository;

import com.interactivehome.main_service.model.events.entity.MovementSensorState;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovementSensorStateRepository extends MongoRepository<MovementSensorState, String> {

}
