package com.interactivehome.main_service.service.events;

import com.interactivehome.main_service.model.events.dto.MotionSensorStateDto;
import com.interactivehome.main_service.model.events.entity.MotionSensorState;
import com.interactivehome.main_service.repository.events.MovementSensorStateRepository;
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
  public void saveState(MotionSensorStateDto dto) {
    MotionSensorState motionSensorState = new MotionSensorState();
    motionSensorState.mapFromDto(dto);
    mongoTemplate.save(motionSensorState);

    // If a movement is caught and the alarm is armed, then buzz the alarm
    buzzAlarm();
  }

  @Override
  public List<MotionSensorState> getSensorActivityByAlarmIdAndSensorId(
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
      return mongoTemplate.find(query, MotionSensorState.class);
    }
    // If the dates are not present then get the latest movement sensor activity
    query.with(new Sort(Direction.DESC, "updated_utc"));
    List<MotionSensorState> motionSensorStateList = new ArrayList<>();
    if(mongoTemplate.find(query, MotionSensorState.class).size() > 0)
      motionSensorStateList.add(mongoTemplate.find(query, MotionSensorState.class).get(0));
    return motionSensorStateList;
  }

  @Override
  public void buzzAlarm() {
  }
}
