package com.interactivehome.main_service.repository;

import com.interactivehome.main_service.model.entity.Battery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatteryRepository extends MongoRepository<Battery, String> {

}
