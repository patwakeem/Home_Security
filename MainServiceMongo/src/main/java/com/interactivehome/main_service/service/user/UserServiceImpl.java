package com.interactivehome.main_service.service.user;

import com.interactivehome.main_service.model.user.dto.UserDto;
import com.interactivehome.main_service.model.user.entity.User;
import com.interactivehome.main_service.repository.user.UserRepository;
import com.mongodb.client.result.DeleteResult;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final MongoTemplate mongoTemplate;

    public UserServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void registerUser(UserDto dto) {
        User user = new User();
        Integer id = getNextId();
        user.createUserFromDto(id, dto);
        mongoTemplate.save(user);
    }

    private Integer getNextId() {
        Query query = new Query();
        query.with(new org.springframework.data.domain.Sort(Sort.Direction.DESC, "_id"));
        if(mongoTemplate.findOne(query, User.class) != null)
            return mongoTemplate.findOne(query, User.class).get_id() + 1;

        return 1;
    }

    @Override
    public User modifyUser(Integer id, UserDto dto) {
        User user = mongoTemplate.findById(id, User.class);

//        user could be null see group service impl comment
        user.updateUserFromDto(dto);
        mongoTemplate.save(user);
        return user;
    }

    @Override
    public User getUserById(Integer id) {
        return mongoTemplate.findById(id, User.class);
    }

    @Override
    public List<User> getAllUsers() {
        return mongoTemplate.findAll(User.class);
    }

    @Override
    public Boolean deleteUser(Integer id) {
        return mongoTemplate.remove(mongoTemplate.findById(id, User.class)).wasAcknowledged();
    }
}
