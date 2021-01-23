package com.interactivehome.main_service.service.device;

import com.interactivehome.main_service.model.device.dto.MotionSensorDto;
import com.interactivehome.main_service.model.device.entity.MotionSensor;
import com.interactivehome.main_service.repository.device.MotionSensorRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
public class MotionSensorServiceImpl implements MotionSensorService {

    private final MotionSensorRepository motionSensorRepository;
    private final MongoTemplate mongoTemplate;

    public MotionSensorServiceImpl(MotionSensorRepository motionSensorRepository,
                                   MongoTemplate mongoTemplate) {
        this.motionSensorRepository = motionSensorRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void registerMotionSensor(MotionSensorDto dto) {
        MotionSensor motionSensor = new MotionSensor();
        Integer id = getNextId();
        motionSensor.createMotionSensorFromDto(id, dto);
        mongoTemplate.save(motionSensor);
    }

    private Integer getNextId() {
        Query query = new Query();

        // The code in these services (device package and events package) looks very clean and nice.
        // The only thing I can suggest is always running intellij's code cleanup and it will automatically
        // catch things like missing spaces (seen in the line below). You can run code cleanup by using the "Code"
        // option menu and click "Reformat Code". Try to run it before you save and commit.
        query.with(new org.springframework.data.domain.Sort(Sort.Direction.DESC, "_id"));
        if(mongoTemplate.findOne(query, MotionSensor.class) != null)
            return mongoTemplate.findOne(query, MotionSensor.class).get_id() + 1;

        return 1;
    }

    @Override
    public MotionSensor modifyMotionSensorByAlarmIdAndId(Integer alarmId, Integer id, MotionSensorDto dto) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        query.addCriteria(Criteria.where("alarm_id").is(alarmId));
        MotionSensor motionSensor = mongoTemplate.findOne(query, MotionSensor.class);

//        good checking if the motion sensor is not found.
//        if its not found do you want to throw an exception and
//        return a 404?
        if(motionSensor != null) {
            motionSensor.updateMotionSensorFromDto(dto);
            mongoTemplate.save(motionSensor);
        }
        return motionSensor;
    }

    @Override
    public MotionSensor getMotionSensorByAlarmIdAndId(Integer alarmId, Integer id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        query.addCriteria(Criteria.where("alarm_id").is(alarmId));
        return mongoTemplate.findOne(query, MotionSensor.class);
    }

    @Override
    public List<MotionSensor> getAllMotionSensorsByAlarmId(Integer alarmId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("alarm_id").is(alarmId));
        return mongoTemplate.find(query, MotionSensor.class);
    }

    @Override
    public void deleteMotionSensor(Integer alarmId, Integer id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        query.addCriteria(Criteria.where("alarm_id").is(alarmId));
        mongoTemplate.remove(Objects.requireNonNull(mongoTemplate.findOne(query, MotionSensor.class))).wasAcknowledged();
    }
}
