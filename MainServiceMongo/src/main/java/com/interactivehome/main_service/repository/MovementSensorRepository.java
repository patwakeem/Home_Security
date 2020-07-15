package com.interactivehome.main_service.repository;

import com.interactivehome.main_service.model.entity.MovementSensor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovementSensorRepository extends MongoRepository<MovementSensor, String> {

}
