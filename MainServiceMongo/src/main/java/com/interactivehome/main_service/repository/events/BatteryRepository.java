package com.interactivehome.main_service.repository.events;

import com.interactivehome.main_service.model.events.entity.Battery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatteryRepository extends MongoRepository<Battery, String> {

}
