package com.interactivehome.main_service.service;

import com.interactivehome.main_service.config.AppProperties;
import com.interactivehome.main_service.model.dto.DoorSensorStateDto;
import com.interactivehome.main_service.model.entity.DoorSensorState;
import com.interactivehome.main_service.repository.DoorSensorStateRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DoorSensorStateServiceImpl implements DoorSensorStateService {

    private final DoorSensorStateRepository doorSensorStateRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    private AppProperties appProperties;

    @Value("$(verification_process_endpoint)")
    private String verificationProcessEndpoint;
    @Value("$(verification_process_timeout_sec)")
    private String verificationProcessTimeoutSec;

    private RestTemplate restTemplate;
    private Timer timer;

    public DoorSensorStateServiceImpl(DoorSensorStateRepository doorSensorStateRepository,
                                      MongoTemplate mongoTemplate) {
        this.doorSensorStateRepository = doorSensorStateRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void saveState(DoorSensorStateDto dto) {
        DoorSensorState doorSensorState = new DoorSensorState();
        doorSensorState.mapFromDto(dto);
        doorSensorStateRepository.save(doorSensorState);
    }

    @Override
    public List<DoorSensorState> getDoorStateByAlarmIdAndDoorIdFromDateToDate(Integer alarmId, Integer doorId, Date fromDate, Date toDate) {
        if(fromDate != null && !fromDate.toString().isEmpty() && (toDate == null || toDate.toString().isEmpty())) {
            toDate = java.util.Date.from(LocalDate.now().atStartOfDay().plusDays(1)
                .atZone(ZoneId.systemDefault())
                .toInstant());
        }

        String messageOut;
        messageOut = "Get door state by door id: " + doorId;
        if(fromDate != null && toDate != null)
            messageOut += " from date: " + fromDate.toString() + ", to date : " + toDate.toString();
        System.out.println(messageOut);

        Query query = new Query();
        query.addCriteria(Criteria.where("alarm_id").is(alarmId));
        query.addCriteria(Criteria.where("door_id").is(doorId));
        if((fromDate != null && toDate != null) && (!fromDate.toString().isEmpty() && !toDate.toString().isEmpty()))
        {
            query.addCriteria(Criteria.where("updated_utc").gte(fromDate).lte(toDate));
            query.with(new org.springframework.data.domain.Sort(Direction.DESC, "updated_utc"));
            return mongoTemplate.find(query, DoorSensorState.class);
        }
        // If the dates are not present then get the latest voltage measurement
        query.with(new org.springframework.data.domain.Sort(Direction.DESC, "updated_utc"));
        List<DoorSensorState> doorSensorStateList = new ArrayList<>();
        if(mongoTemplate.find(query, DoorSensorState.class) != null && mongoTemplate.find(query, DoorSensorState.class).size() > 0)
            doorSensorStateList.add(mongoTemplate.find(query, DoorSensorState.class).get(0));
        return doorSensorStateList;
    }

    @Override
    public DoorSensorState getLastDoorByAlarmIdAndDoorId(DoorSensorStateDto dto) {
        DoorSensorState doorSensorState = new DoorSensorState();
        Query query = new Query();
        query.addCriteria(Criteria.where("alarm_id").is(dto.alarmId));
        query.addCriteria(Criteria.where("door_id").is(dto.doorId));
        query.with(new org.springframework.data.domain.Sort(Direction.DESC, "updated_utc"));
        List<DoorSensorState> list = mongoTemplate.find(query, DoorSensorState.class);
        if(list.size() > 0)
            doorSensorState = list.get(0);

        return doorSensorState;
    }

    @Override
    public void saveBatteryVoltage(DoorSensorStateDto doorSensorStateDto) {
        DoorSensorState doorSensorState = getLastDoorByAlarmIdAndDoorId(doorSensorStateDto);

        doorSensorState.mapFromDto(doorSensorStateDto);
        doorSensorStateRepository.save(doorSensorState);
    }
}
