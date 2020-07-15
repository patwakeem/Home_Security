package com.interactivehome.main_service.service;

import com.interactivehome.main_service.model.dto.DoorSensorDto;
import com.interactivehome.main_service.model.entity.DoorSensor;
import com.interactivehome.main_service.repository.DoorStateRepository;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class DoorStateServiceImpl implements DoorStateService {

    private final DoorStateRepository doorRepository;
    private final MongoTemplate mongoTemplate;

    public DoorStateServiceImpl(DoorStateRepository doorStateRepository,
                                MongoTemplate mongoTemplate) {
        this.doorRepository = doorStateRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void saveState(DoorSensorDto dto) {
        DoorSensor doorSensor = new DoorSensor();
        doorSensor.mapFromDto(dto);
        doorRepository.save(doorSensor);
    }

    @Override
    public DoorSensor getDoorStateByDoorId(Integer doorId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("doorId").is(doorId));
        query.with(new Sort(Direction.DESC, "updatedUtc"));
        return mongoTemplate.findOne(query, DoorSensor.class);
    }

  @Override
  public List<DoorSensor> getDoorStateByDoorIdFromDateToDate(Integer doorId, Date fromDate, Date toDate) {
      Query query = new Query();
      query.addCriteria(Criteria.where("doorId").is(doorId));
      query.addCriteria(Criteria.where("updatedUtc").gte(fromDate).lt(toDate));
      query.with(new Sort(Direction.DESC, "updatedUtc"));
      return mongoTemplate.find(query, DoorSensor.class);
  }
}
