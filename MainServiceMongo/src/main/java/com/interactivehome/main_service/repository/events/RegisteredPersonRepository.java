package com.interactivehome.main_service.repository.events;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisteredPersonRepository extends MongoRepository<RegisteredPersonRepository, String> {

}
