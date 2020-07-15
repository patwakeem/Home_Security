package com.interactivehome.main_service.service;

import com.interactivehome.main_service.model.dto.TemperatureHumidiryGasDto;
import com.interactivehome.main_service.model.entity.TemperatureHumidityGas;
import com.interactivehome.main_service.repository.TemperatureHumidityGasRepository;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class TemperatureHumidityGasServiceImpl implements TemperatureHumidityGasService {
  private final TemperatureHumidityGasRepository temperatureHumidityGasRepository;

  private final MongoTemplate mongoTemplate;

  public TemperatureHumidityGasServiceImpl(TemperatureHumidityGasRepository temperatureHumidityGasRepository,
                                            MongoTemplate mongoTemplate) {
    this.temperatureHumidityGasRepository = temperatureHumidityGasRepository;
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public void saveValues(TemperatureHumidiryGasDto dto) {
    TemperatureHumidityGas temperatureHumidityGas = new TemperatureHumidityGas();
    temperatureHumidityGas.mapFromDto(dto);
    temperatureHumidityGasRepository.save(temperatureHumidityGas);
  }

  @Override
  public List<TemperatureHumidityGas> getAll() {
    return mongoTemplate.findAll(TemperatureHumidityGas.class);
  }

  @Override
  public List<TemperatureHumidityGas> getAllBySensorId(Integer sensorId) {
    Query query = new Query();
    query.addCriteria(Criteria.where("sensorId").is(sensorId));
    query.with(new Sort(Direction.DESC, "updatedUtc"));
    return mongoTemplate.find(query, TemperatureHumidityGas.class);
  }

  @Override
  public List<TemperatureHumidityGas> getValuesBySensorIdFromDateToDate(
                                          Integer sensorId, Date fromDate, Date toDate) {
    System.out.println("From date: " + fromDate.toString() + ", to date : " + toDate.toString());
    Query query = new Query();
    query.addCriteria(Criteria.where("sensorId").is(sensorId));
    query.addCriteria(Criteria.where("updatedUtc").gte(fromDate).lt(toDate));
    query.with(new Sort(Direction.DESC, "updatedUtc"));
    return mongoTemplate.find(query, TemperatureHumidityGas.class);
  }
}
