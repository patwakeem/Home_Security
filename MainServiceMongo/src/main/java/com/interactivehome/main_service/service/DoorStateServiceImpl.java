package com.interactivehome.main_service.service;

import com.interactivehome.main_service.config.AppProperties;
import com.interactivehome.main_service.model.dto.DoorSensorDto;
import com.interactivehome.main_service.model.entity.DoorSensor;
import com.interactivehome.main_service.repository.DoorStateRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DoorStateServiceImpl implements DoorStateService {

    private final DoorStateRepository doorRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    private AppProperties appProperties;

    @Value("$(verification_process_endpoint)")
    private String verificationProcessEndpoint;
    @Value("$(verification_process_timeout_sec)")
    private String verificationProcessTimeoutSec;

    private RestTemplate restTemplate;
    private Timer timer;

    public DoorStateServiceImpl(DoorStateRepository doorStateRepository,
                                MongoTemplate mongoTemplate) {
        this.doorRepository = doorStateRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void saveState(DoorSensorDto dto) {
        DoorSensor doorSensor = new DoorSensor();
        doorSensor.mapFromDto(dto);
        doorRepository.save(doorSensor);
    }

    @Override
    public List<DoorSensor> getDoorStateByDoorIdFromDateToDate(Integer doorId, Date fromDate, Date toDate) {
        if(fromDate != null && !fromDate.toString().isEmpty() && (toDate == null || toDate.toString().isEmpty())) {
            toDate = java.util.Date.from(LocalDate.now().atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
        }

        String messageOut;
        messageOut = "Get door state by door id: " + doorId;
        if(fromDate != null && toDate != null)
            messageOut += " from date: " + fromDate.toString() + ", to date : " + toDate.toString();
        System.out.println(messageOut);

        Query query = new Query();
        query.addCriteria(Criteria.where("doorId").is(doorId));
        if((fromDate != null && toDate != null) && (!fromDate.toString().isEmpty() && !toDate.toString().isEmpty())) {
            query.addCriteria(Criteria.where("updatedUtc").gte(fromDate).lt(toDate));
            query.with(new Sort(Direction.DESC, "updatedUtc"));
            return mongoTemplate.find(query, DoorSensor.class);
        }
        // If the dates are not present then get the latest door state
        query.with(new Sort(Direction.DESC, "updatedUtc"));
        List<DoorSensor> doorSensorList = null;
        doorSensorList.add(mongoTemplate.find(query, DoorSensor.class).get(0));
        return doorSensorList;
  }
}
