package com.interactivehome.main_service.repository;

import com.interactivehome.main_service.model.entity.DoorSensor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoorStateRepository extends MongoRepository<DoorSensor, String> {
}
