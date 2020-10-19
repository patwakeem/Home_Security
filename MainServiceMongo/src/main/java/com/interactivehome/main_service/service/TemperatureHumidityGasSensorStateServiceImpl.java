package com.interactivehome.main_service.service;

import com.interactivehome.main_service.model.dto.TemperatureHumidiryGasSensorStateDto;
import com.interactivehome.main_service.model.entity.TemperatureHumidityGasSensorState;
import com.interactivehome.main_service.repository.TemperatureHumidityGasSensorStateRepository;
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
public class TemperatureHumidityGasSensorStateServiceImpl implements TemperatureHumidityGasSensorStateService {
  private final TemperatureHumidityGasSensorStateRepository temperatureHumidityGasSensorStateRepository;

  private final MongoTemplate mongoTemplate;

  public TemperatureHumidityGasSensorStateServiceImpl(TemperatureHumidityGasSensorStateRepository temperatureHumidityGasSensorStateRepository,
                                                      MongoTemplate mongoTemplate) {
    this.temperatureHumidityGasSensorStateRepository = temperatureHumidityGasSensorStateRepository;
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public void saveValues(TemperatureHumidiryGasSensorStateDto dto) {
    TemperatureHumidityGasSensorState temperatureHumidityGasSensorState = new TemperatureHumidityGasSensorState();
    temperatureHumidityGasSensorState.mapFromDto(dto);
    temperatureHumidityGasSensorStateRepository.save(temperatureHumidityGasSensorState);
  }

  @Override
  public List<TemperatureHumidityGasSensorState> getValuesByAlarmIdAndSensorIdFromDateToDate(
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
    query.addCriteria(Criteria.where("sensor_id").is(sensorId));
    if((fromDate != null && toDate != null) && (!fromDate.toString().isEmpty() && !toDate.toString().isEmpty()))
    {
      query.addCriteria(Criteria.where("updatedUtc").gte(fromDate).lte(toDate));
      query.with(new org.springframework.data.domain.Sort(Direction.DESC, "updatedUtc"));
      return mongoTemplate.find(query, TemperatureHumidityGasSensorState.class);
    }
    // If the dates are not present then get the sensors measurements
    query.with(new org.springframework.data.domain.Sort(Direction.DESC, "updatedUtc"));
    List<TemperatureHumidityGasSensorState> temperatureHumidityGasSensorStateList = new ArrayList<>();
    if(mongoTemplate.find(query, TemperatureHumidityGasSensorState.class).size() > 0)
      temperatureHumidityGasSensorStateList.add(mongoTemplate.find(query, TemperatureHumidityGasSensorState.class).get(0));
    return temperatureHumidityGasSensorStateList;
  }
}
