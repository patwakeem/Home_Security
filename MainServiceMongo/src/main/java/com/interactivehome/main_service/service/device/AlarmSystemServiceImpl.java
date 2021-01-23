package com.interactivehome.main_service.service.device;

import com.interactivehome.main_service.model.device.dto.AlarmSystemDto;
import com.interactivehome.main_service.model.device.entity.AlarmSystem;
import com.interactivehome.main_service.repository.device.AlarmSystemRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlarmSystemServiceImpl implements AlarmSystemService {

//    This repository isn't used, if you want to use MongoTemplate for mongo operations you can
//    Using the repository is easier with Intellij pro because it will autocomplete your methods for you
//    With community edition it might be tough though.
//
//    When you have the new job try to have them get pro for you so you can use repositories.
    private final AlarmSystemRepository alarmSystemRepository;
    private final MongoTemplate mongoTemplate;

    public AlarmSystemServiceImpl(AlarmSystemRepository alarmSystemRepository,
                                 MongoTemplate mongoTemplate) {
        this.alarmSystemRepository = alarmSystemRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void registerAlarmSystem(AlarmSystemDto dto) {
        AlarmSystem alarmSystem = new AlarmSystem();
        Integer id = getNextId();
        alarmSystem.createAlarmFromDto(id, dto);
        mongoTemplate.save(alarmSystem);
    }

    private Integer getNextId() {
        Query query = new Query();
        query.with(new org.springframework.data.domain.Sort(Sort.Direction.DESC, "_id"));
        if(mongoTemplate.findOne(query, AlarmSystem.class) != null)
            return mongoTemplate.findOne(query, AlarmSystem.class).get_id() + 1;

        return 1;
    }

    @Override
    public AlarmSystem modifyAlarmSystemById(Integer id, AlarmSystemDto dto) {
        AlarmSystem alarmSystem = mongoTemplate.findById(id, AlarmSystem.class);
        alarmSystem.updateAlarmFromDto(dto);
        mongoTemplate.save(alarmSystem);
        return alarmSystem;
    }

    @Override
    public AlarmSystem getAlarmSystemById(Integer id) {
        return mongoTemplate.findById(id, AlarmSystem.class);
    }

    @Override
    public List<AlarmSystem> getAllAlarmSystems() {
        return mongoTemplate.findAll(AlarmSystem.class);
    }

    @Override
    public void deleteAlarmSystem(Integer id) {
        mongoTemplate.remove(mongoTemplate.findById(id, AlarmSystem.class)).wasAcknowledged();
    }
}
