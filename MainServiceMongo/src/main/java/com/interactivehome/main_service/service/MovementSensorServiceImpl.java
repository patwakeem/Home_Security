package com.interactivehome.main_service.service;

import com.interactivehome.main_service.model.dto.MovementSensorDto;
import com.interactivehome.main_service.model.entity.MovementSensor;
import com.interactivehome.main_service.repository.MovementSensorRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    // If a movement is caught and the alarm is armed, then buzz the alarm
    buzzAlarm();
  }

  @Override
  public List<MovementSensor> getMovementSensorActivityByMovementSensorId(Integer movementSensorId, Date fromDate, Date toDate) {
    if(fromDate != null && !fromDate.toString().isEmpty() && (toDate == null || toDate.toString().isEmpty())) {
      toDate = java.util.Date.from(LocalDate.now().atStartOfDay().plusDays(1)
          .atZone(ZoneId.systemDefault())
          .toInstant());
    }

    String messageOut;
    messageOut = "Get movement activity by movement sensro id: " + movementSensorId;
    if(fromDate != null && toDate != null)
      messageOut += " from date: " + fromDate.toString() + ", to date : " + toDate.toString();
    System.out.println(messageOut);

    Query query = new Query();
    query.addCriteria(Criteria.where("movementSensorId").is(movementSensorId));
    if((fromDate != null && toDate != null) && (!fromDate.toString().isEmpty() && !toDate.toString().isEmpty())) {
      query.addCriteria(Criteria.where("updatedUtc").gte(fromDate).lte(toDate));
      query.with(new Sort(Direction.DESC, "updatedUtc"));
      return mongoTemplate.find(query, MovementSensor.class);
    }
    // If the dates are not present then get the latest movement sensor activity
    query.with(new Sort(Direction.DESC, "updatedUtc"));
    List<MovementSensor> movementSensorList = new ArrayList<>();
    if(mongoTemplate.find(query, MovementSensor.class).size() > 0)
      movementSensorList.add(mongoTemplate.find(query, MovementSensor.class).get(0));
    return movementSensorList;
  }

  @Override
  public void buzzAlarm() {
  }
}
