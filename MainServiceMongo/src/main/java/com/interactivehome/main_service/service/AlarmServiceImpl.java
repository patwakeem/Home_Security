package com.interactivehome.main_service.service;

import com.interactivehome.main_service.model.dto.AlarmDto;
import com.interactivehome.main_service.model.entity.Alarm;
import com.interactivehome.main_service.repository.AlarmRepository;
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
public class AlarmServiceImpl implements AlarmService {
  private final AlarmRepository alarmRepository;
  private final MongoTemplate mongoTemplate;

  public AlarmServiceImpl(AlarmRepository alarmRepository,
                          MongoTemplate mongoTemplate) {
    this.alarmRepository = alarmRepository;
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public void saveAlarmState(AlarmDto dto) {
    Alarm alarm = new Alarm();
    alarm.mapFromDto(dto);
    alarmRepository.save(alarm);

  }

  @Override
  public Integer getAlarmStateByAlarmId(Integer alarmId) {
    Query query = new Query();
    query.addCriteria(Criteria.where("alarmId").is(alarmId));
    query.with(new Sort(Direction.DESC, "updatedUtc"));
    Alarm alarm = new Alarm();
    if(mongoTemplate.findOne(query, Alarm.class) != null)
      alarm = mongoTemplate.findOne(query, Alarm.class);
    else { // If the alarm is not set, then initialize one
      alarm.setAlarmId(1);
      alarm.setAlarmOn(false);
      alarm.setAlarmState(0);
      alarm.setUpdatedUtc(new Date(System.currentTimeMillis()));
      alarmRepository.save(alarm);
    }
    System.out.println("Returning alarm state: " + alarm.getAlarmState());
    return alarm.getAlarmState();
  }

  @Override
  public Boolean getAlarmOnByAlarmId(Integer alarmId) {
    Query query = new Query();
    query.addCriteria(Criteria.where("alarmId").is(alarmId));
    query.with(new Sort(Direction.DESC, "updatedUtc"));
    Alarm alarm = new Alarm();
    if(mongoTemplate.findOne(query, Alarm.class) != null)
      alarm = mongoTemplate.findOne(query, Alarm.class);
    else { // If the alarm is not set, then initialize one
      alarm.setAlarmId(1);
      alarm.setAlarmOn(false);
      alarm.setAlarmState(0);
      alarm.setUpdatedUtc(new Date(System.currentTimeMillis()));
      alarmRepository.save(alarm);
    }
    return alarm.getAlarmOn();
  }

  @Override
  public List<Alarm> getAlarmByAlarmIdFromDateToDate(Integer alarmId, Date fromDate, Date toDate) {
    if(fromDate != null && !fromDate.toString().isEmpty() && (toDate == null || toDate.toString().isEmpty())) {
      toDate = java.util.Date.from(LocalDate.now().atStartOfDay().plusDays(1)
          .atZone(ZoneId.systemDefault())
          .toInstant());
    }

    String messageOut;
    messageOut = "Get alarm state by alarm id: " + alarmId;
    if(fromDate != null && toDate != null)
      messageOut += " from date: " + fromDate.toString() + ", to date : " + toDate.toString();
    System.out.println(messageOut);

    Query query = new Query();
    query.addCriteria(Criteria.where("alarm_id").is(alarmId));
    if((fromDate != null && toDate != null) && (!fromDate.toString().isEmpty() && !toDate.toString().isEmpty()))
    {
      query.addCriteria(Criteria.where("updated_utc").gte(fromDate).lt(toDate));
      query.with(new org.springframework.data.domain.Sort(Direction.DESC, "updated_utc"));
      return mongoTemplate.find(query, Alarm.class);
    }
    // If the dates are not present then get the latest voltage measurement
    query.with(new org.springframework.data.domain.Sort(Direction.DESC, "updated_utc"));
    List<Alarm> alarmList = new ArrayList<>();
    if(mongoTemplate.find(query, Alarm.class) != null)
      alarmList.add(mongoTemplate.find(query, Alarm.class).get(0));
    return alarmList;

  }

  @Override
  public void stopAlarm(AlarmDto dto) {
    Alarm alarm = new Alarm();
    // In case wrong payload passed, fix it with the proper values
    // When the alarm goes off and we want to stop it, we set the alarmOn state to false and disarm the alarm
    dto.setAlarmOn(false);
    dto.setAlarmState(0);
    alarm.mapFromDto(dto);
    alarmRepository.save(alarm);
  }
}
