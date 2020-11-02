package com.interactivehome.main_service.service.device;

import com.interactivehome.main_service.model.device.dto.AlarmSystemDto;
import com.interactivehome.main_service.model.device.entity.AlarmSystem;
import com.interactivehome.main_service.repository.device.AlarmSystemRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlarmSystemServiceImpl implements AlarmSystemService {
    private final AlarmSystemRepository alarmSystemRepository;
    private final MongoTemplate mongoTemplate;

    public AlarmSystemServiceImpl(AlarmSystemRepository alarmSystemRepository,
                                 MongoTemplate mongoTemplate) {
        this.alarmSystemRepository = alarmSystemRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void addAlarmSystem(AlarmSystemDto dto) {
        AlarmSystem alarmSystem = new AlarmSystem();
        dto.alarmId = getAlarmSystemNewId();
        alarmSystem.mapFromDto(dto);
        mongoTemplate.save(alarmSystem);
    }

    @Override
    public Integer getAlarmSystemNewId() {
        Integer alarmId = -1;
        Query query = new Query();
        query.with(new org.springframework.data.domain.Sort(Sort.Direction.DESC, "alarm_id"));
        if(mongoTemplate.find(query, AlarmSystem.class).isEmpty()) {
            return 1;
        } else {
            alarmId = mongoTemplate.find(query, AlarmSystem.class).get(0).getAlarmId() + 1;
        }
        return alarmId;
    }

    @Override
    public void deleteAlarmSystem(AlarmSystemDto dto) {
        AlarmSystem alarmSystem = new AlarmSystem();
        alarmSystem.mapFromDto(dto);
        mongoTemplate.remove(alarmSystem);
    }

    @Override
    public void modifyAlarmSystem(AlarmSystemDto dto) {
        AlarmSystem alarmSystem = new AlarmSystem();
        alarmSystem.setAlarmId(dto.alarmId);
        alarmSystem.setEnabled(dto.enabled);
        alarmSystem.setDescription(dto.description);
        alarmSystem.setActiveAlarm(dto.activeAlarm);
        mongoTemplate.save(alarmSystem);
    }

    @Override
    public void setActiveAlarmSystem(AlarmSystemDto dto) {
        Query query = new Query();
        query.addCriteria(Criteria.where("active_alarm").is(true));
        if(!mongoTemplate.find(query, AlarmSystem.class).isEmpty()) {
            mongoTemplate.findAndModify(query, Update.update("active_alarm", false), AlarmSystem.class);
        }
        query = new Query();
        query.addCriteria(Criteria.where("alarm_id").is(dto.getAlarmId()));
        if(!mongoTemplate.find(query, AlarmSystem.class).isEmpty()) {
            mongoTemplate.findAndModify(query, Update.update("active_alarm", true), AlarmSystem.class);
        }
    }

    @Override
    public AlarmSystem getActiveAlarmSystem() {
        Query query = new Query();
        query.addCriteria(Criteria.where("active_alarm").is(true));
        if(!mongoTemplate.find(query, AlarmSystem.class).isEmpty()) {
            return mongoTemplate.findOne(query, AlarmSystem.class);
        }
        return new AlarmSystem();
    }

    @Override
    public List<AlarmSystem> getAllAlarmSystems() {
        Query query = new Query();
        query.with(new org.springframework.data.domain.Sort(Sort.Direction.DESC, "alarm_id"));
        if(!mongoTemplate.find(query, AlarmSystem.class).isEmpty()) {
            return mongoTemplate.find(query, AlarmSystem.class);
        }
        return new ArrayList<>();
    }
}
