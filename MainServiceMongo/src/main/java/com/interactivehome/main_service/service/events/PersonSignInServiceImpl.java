package com.interactivehome.main_service.service.events;

import com.interactivehome.main_service.model.events.dto.PersonSignInDto;
import com.interactivehome.main_service.model.events.entity.PersonSignIn;
import com.interactivehome.main_service.repository.PersonSignInRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class PersonSignInServiceImpl implements PersonSignInService {
  @Autowired
  private RegisteredPersonService registeredPersonService;

  private final PersonSignInRepository personSignInRepository;
  private final MongoTemplate mongoTemplate;

  PersonSignInServiceImpl(PersonSignInRepository personSignInRepository,
                          MongoTemplate mongoTemplate) {
    this.personSignInRepository = personSignInRepository;
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public void savePersonSingIn(PersonSignInDto dto) {
    PersonSignIn personSignIn = new PersonSignIn();
    personSignIn.mapFromDto(dto);
    personSignIn.setName(registeredPersonService.getRegisteredPersonByAlarmIdAndRfidCardId(dto.alarmId, dto.rfidCardId).getName());
    mongoTemplate.save(personSignIn);
  }

  @Override
  public List<PersonSignIn> getPersonsSignedInByAlarmIdAndRfidCardIdFromDateToDate(Integer alarmId, String rfidCardId, Date fromDate, Date toDate) {
    if(fromDate != null && !fromDate.toString().isEmpty() && (toDate == null || toDate.toString().isEmpty())) {
      toDate = java.util.Date.from(LocalDate.now().atStartOfDay().plusDays(1)
          .atZone(ZoneId.systemDefault())
          .toInstant());
    }

    String messageOut;
    messageOut = "Get persons signed by rfid card id: " + rfidCardId;
    if(fromDate != null && toDate != null)
      messageOut += " from date: " + fromDate.toString() + ", to date : " + toDate.toString();
    System.out.println(messageOut);

    Query query = new Query();
    query.addCriteria(Criteria.where("alarm_id").is(alarmId));
    query.addCriteria(Criteria.where("rfid_card_id").is(rfidCardId));
    if((fromDate != null && toDate != null) && (!fromDate.toString().isEmpty() && !toDate.toString().isEmpty()))
    {
      query.addCriteria(Criteria.where("updated_utc").gte(fromDate).lte(toDate));
      query.with(new org.springframework.data.domain.Sort(Direction.DESC, "updated_utc"));
      return mongoTemplate.find(query, PersonSignIn.class);
    }
    // If the dates are not present then get the latest voltage measurement
    query.with(new org.springframework.data.domain.Sort(Direction.DESC, "updated_utc"));
    List<PersonSignIn> personSignInList = new ArrayList<>();
    if(mongoTemplate.find(query, PersonSignIn.class).size() > 0)
      personSignInList.add(mongoTemplate.find(query, PersonSignIn.class).get(0));
    return personSignInList;
  }
}
