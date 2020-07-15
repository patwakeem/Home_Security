package com.interactivehome.main_service.service;

import com.interactivehome.main_service.model.dto.MovementSensorDto;
import com.interactivehome.main_service.model.entity.MovementSensor;
import com.interactivehome.main_service.repository.MovementSensorRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class MovementSensorServiceImpl implements MovementSensorService{
  private final MovementSensorRepository movementSensorRepository;
  private final MongoTemplate mongoTemplate;

  public MovementSensorServiceImpl(MovementSensorRepository movementSensorRepository,
                                    MongoTemplate mongoTemplate) {
    this.movementSensorRepository = movementSensorRepository;
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public void saveState(MovementSensorDto dto) {
    MovementSensor movementSensor = new MovementSensor();
    movementSensor.mapFromDto(dto);
    mongoTemplate.save(movementSensor);
  }

  @Override
  public MovementSensor getMovementSensorStateByMovementSensorId(Integer movementSensorId) {
    Query query = new Query();
    query.addCriteria(Criteria.where("movementSensorId").is(movementSensorId));
    query.with(new Sort(Direction.DESC, "updatedUtc"));
    return mongoTemplate.findOne(query, MovementSensor.class);
  }

  @Override
  public void buzzAlarm() {

  }
}
