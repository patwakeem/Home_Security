package com.interactivehome.main_service.service.events;

import com.interactivehome.main_service.model.events.dto.EnvironmentSensorStateDto;
import com.interactivehome.main_service.model.events.entity.EnvironmentSensorState;
import com.interactivehome.main_service.repository.events.EnvironmentSensorStateRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class EnvironmentSensorStateServiceImpl implements EnvironmentSensorStateService {
  private final EnvironmentSensorStateRepository environmentSensorStateRepository;

  private final MongoTemplate mongoTemplate;

  public EnvironmentSensorStateServiceImpl(EnvironmentSensorStateRepository environmentSensorStateRepository,
                                           MongoTemplate mongoTemplate) {
    this.environmentSensorStateRepository = environmentSensorStateRepository;
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public void saveValuesBySensorId(Integer id, EnvironmentSensorStateDto dto) {
    EnvironmentSensorState environmentSensorState = new EnvironmentSensorState();
    environmentSensorState.mapFromDto(id, dto);
    environmentSensorStateRepository.save(environmentSensorState);
  }

  @Override
  public List<EnvironmentSensorState> getValuesByAlarmIdAndSensorIdFromDateToDate(
                                          Integer alarmId, Integer sensorId, Date fromDate, Date toDate) {
    if(fromDate != null && !fromDate.toString().isEmpty() && (toDate == null || toDate.toString().isEmpty()))
      toDate = java.util.Date.from(LocalDate.now().atStartOfDay().plusDays(1)
          .atZone(ZoneId.systemDefault())
          .toInstant());

    String messageOut;
    messageOut = "Get temperature humidity gas values by alarm id: " + alarmId + " and sensor id: " + sensorId;
    if(fromDate != null && toDate != null)
      messageOut += " from date: " + fromDate.toString() + ", to date : " + toDate.toString();
    System.out.println(messageOut);

    Query query = new Query();
    query.addCriteria(Criteria.where("alarm_id").is(alarmId));
    query.addCriteria(Criteria.where("_id").is(sensorId));
    if((fromDate != null && toDate != null) && (!fromDate.toString().isEmpty() && !toDate.toString().isEmpty())) {
      query.addCriteria(Criteria.where("updated_utc").gte(fromDate).lte(toDate));
      query.with(new org.springframework.data.domain.Sort(Direction.DESC, "updated_utc"));
      return mongoTemplate.find(query, EnvironmentSensorState.class);
    }
    // If the dates are not present then get the sensors measurements
    query.with(new org.springframework.data.domain.Sort(Direction.DESC, "updated_utc"));
    List<EnvironmentSensorState> environmentSensorStateList = new ArrayList<>();
    if(mongoTemplate.find(query, EnvironmentSensorState.class).size() > 0)
      environmentSensorStateList.add(mongoTemplate.find(query, EnvironmentSensorState.class).get(0));
    return environmentSensorStateList;
  }
}
