package com.interactivehome.main_service.service.user;

import com.interactivehome.main_service.model.user.dto.GroupDto;
import com.interactivehome.main_service.model.user.entity.Group;
import com.interactivehome.main_service.model.user.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {
    private final MongoTemplate mongoTemplate;

    public GroupServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void createGroup(GroupDto dto) {
        Group group = new Group();
        Integer id = getNextId();
        group.set_id(id);
        group.setName(dto.getName());
        mongoTemplate.save(group);
    }

    private Integer getNextId() {
        Query query = new Query();
        query.with(new org.springframework.data.domain.Sort(Sort.Direction.DESC, "_id"));
        if(mongoTemplate.findOne(query, Group.class) != null)
            return mongoTemplate.findOne(query, Group.class).get_id() + 1;

        return 1;
    }

    @Override
    public Group modifyGroup(Integer id, GroupDto dto) {
        Group group = mongoTemplate.findById(id, Group.class);
        group.setName(dto.getName());
        mongoTemplate.save(group);
        return group;
    }

    @Override
    public Group getGroupById(Integer id) {
        return mongoTemplate.findById(id, Group.class);
    }

    @Override
    public List<Group> getAllGroups() {
        return mongoTemplate.findAll(Group.class);
    }

    @Override
    public Boolean deleteGroup(Integer id) {
        return mongoTemplate.remove(mongoTemplate.findById(id, Group.class)).wasAcknowledged();
    }
}
