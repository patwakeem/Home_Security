package com.interactivehome.main_service.service;

import com.interactivehome.main_service.model.dto.AlarmDto;
import com.interactivehome.main_service.model.entity.Alarm;
import com.interactivehome.main_service.repository.AlarmRepository;
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
  public Alarm getAlarmStateByAlarmId(Integer alarmId) {
    Query query = new Query();
    query.addCriteria(Criteria.where("alarmId").is(alarmId));
    query.with(new Sort(Direction.DESC, "updatedUtc"));
    return mongoTemplate.findOne(query, Alarm.class);
  }

  @Override
  public List<Alarm> getAlarmStateByAlarmIdFromDateToDate(Integer alarmId, Date fromDate, Date toDate) {
    System.out.println("Get alarm state for alarm id: " + alarmId + " from date: " + fromDate.toString() + ", to date : " + toDate.toString());
    Query query = new Query();
    query.addCriteria(Criteria.where("alarmId").is(alarmId));
    query.addCriteria(Criteria.where("updatedUtc").gte(fromDate).lt(toDate));
    query.with(new Sort(Direction.DESC, "updatedUtc"));
    return mongoTemplate.find(query, Alarm.class);
  }
}
