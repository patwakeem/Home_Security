package com.interactivehome.main_service.repository.user;

import com.interactivehome.main_service.model.user.entity.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends MongoRepository<Group, String> {
}
