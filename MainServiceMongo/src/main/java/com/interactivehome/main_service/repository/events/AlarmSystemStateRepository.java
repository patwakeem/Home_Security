package com.interactivehome.main_service.repository.events;

import com.interactivehome.main_service.model.events.entity.AlarmSystemState;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmSystemStateRepository extends MongoRepository<AlarmSystemState, String> {

}
