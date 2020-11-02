package com.interactivehome.main_service.service;

import com.interactivehome.main_service.model.dto.MovementSensorStateDto;
import com.interactivehome.main_service.model.entity.MovementSensorState;
import com.interactivehome.main_service.repository.MovementSensorStateRepository;
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
public class MovementSensorStateServiceImpl implements MovementSensorStateService {
  private final MovementSensorStateRepository movementSensorStateRepository;
  private final MongoTemplate mongoTemplate;

  public MovementSensorStateServiceImpl(MovementSensorStateRepository movementSensorStateRepository,
                                        MongoTemplate mongoTemplate) {
    this.movementSensorStateRepository = movementSensorStateRepository;
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public void saveState(MovementSensorStateDto dto) {
    MovementSensorState movementSensorState = new MovementSensorState();
    movementSensorState.mapFromDto(dto);
    mongoTemplate.save(movementSensorState);

    // If a movement is caught and the alarm is armed, then buzz the alarm
    buzzAlarm();
  }

  @Override
  public List<MovementSensorState> getSensorActivityByAlarmIdAndSensorId(
          Integer alarmId,
          Integer sensorId,
          Date fromDate,
          Date toDate) {

    if(fromDate != null && !fromDate.toString().isEmpty() && (toDate == null || toDate.toString().isEmpty())) {
      toDate = java.util.Date.from(LocalDate.now().atStartOfDay().plusDays(1)
          .atZone(ZoneId.systemDefault())
          .toInstant());
    }

    String messageOut;
    messageOut = "Get movement activity by movement sensor id: " + sensorId;
    if(fromDate != null && toDate != null)
      messageOut += " from date: " + fromDate.toString() + ", to date : " + toDate.toString();
    System.out.println(messageOut);

    Query query = new Query();
    query.addCriteria(Criteria.where("alarm_id").is(alarmId));
    query.addCriteria(Criteria.where("sensor_id").is(sensorId));
    if((fromDate != null && toDate != null) && (!fromDate.toString().isEmpty() && !toDate.toString().isEmpty())) {
      query.addCriteria(Criteria.where("updated_utc").gte(fromDate).lte(toDate));
      query.with(new Sort(Direction.DESC, "updated_utc"));
      return mongoTemplate.find(query, MovementSensorState.class);
    }
    // If the dates are not present then get the latest movement sensor activity
    query.with(new Sort(Direction.DESC, "updated_utc"));
    List<MovementSensorState> movementSensorStateList = new ArrayList<>();
    if(mongoTemplate.find(query, MovementSensorState.class).size() > 0)
      movementSensorStateList.add(mongoTemplate.find(query, MovementSensorState.class).get(0));
    return movementSensorStateList;
  }

  @Override
  public void buzzAlarm() {
  }
}
