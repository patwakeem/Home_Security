package com.interactivehome.main_service.service;

import com.interactivehome.main_service.model.dto.BatteryDto;
import com.interactivehome.main_service.model.entity.Battery;
import com.interactivehome.main_service.repository.BatteryRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class BatteryServiceImpl implements BatteryService {
  private final BatteryRepository batteryRepository;
  private final MongoTemplate mongoTemplate;

  public BatteryServiceImpl(BatteryRepository batteryRepository,
                            MongoTemplate mongoTemplate) {
    this.batteryRepository = batteryRepository;
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public void saveVoltage(BatteryDto dto) {
    Battery battery = new Battery();
    battery.mapFromDto(dto);
    batteryRepository.save(battery);
  }

  @Override
  public List<Battery> getVoltageByBatteryIdFromDateToDate(Integer batteryId, Date fromDate, Date toDate) {
    if(fromDate != null && !fromDate.toString().isEmpty() && (toDate == null || toDate.toString().isEmpty())) {
      toDate = java.util.Date.from(LocalDate.now().atStartOfDay()
          .atZone(ZoneId.systemDefault())
          .toInstant());
    }

    String messageOut;
    messageOut = "Get battery voltage by battery id: " + batteryId;
    if(fromDate != null && toDate != null)
      messageOut += " from date: " + fromDate.toString() + ", to date : " + toDate.toString();
    System.out.println(messageOut);

    Query query = new Query();
    query.addCriteria(Criteria.where("batteryId").is(batteryId));
    if((fromDate != null && toDate != null) && (!fromDate.toString().isEmpty() && !toDate.toString().isEmpty()))
    {
      query.addCriteria(Criteria.where("updatedUtc").gte(fromDate).lt(toDate));
      query.with(new org.springframework.data.domain.Sort(Direction.DESC, "updatedUtc"));
      return mongoTemplate.find(query, Battery.class);
    }
    // If the dates are not present then get the latest voltage measurement
    query.with(new org.springframework.data.domain.Sort(Direction.DESC, "updatedUtc"));
    List<Battery> batteryList = null;
    if(mongoTemplate.find(query, Battery.class).size() > 0)
      batteryList.add(mongoTemplate.find(query, Battery.class).get(0));
    return batteryList;
  }
}
