package com.interactivehome.main_service.service;

import com.interactivehome.main_service.model.dto.PersonSignInDto;
import com.interactivehome.main_service.model.entity.PersonSignIn;
import com.interactivehome.main_service.repository.PersonSignInRepository;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
    personSignIn.setName(registeredPersonService.getRegisteredPersonByRfidCardId(dto.rfidCardId).getName());
    mongoTemplate.save(personSignIn);
  }

  @Override
  public List<PersonSignIn> getPersonsSignedInByRfidCardIdFromDateToDate(String rfidCardId, Date fromDate, Date toDate) {
    System.out.println("Get persons signed by rfid card id: " + rfidCardId + " from date: " + fromDate.toString() + ", to date : " + toDate.toString());
    Query query = new Query();
    query.addCriteria(Criteria.where("rfidCardId").is(rfidCardId));
    query.addCriteria(Criteria.where("updatedUtc").gte(fromDate).lt(toDate));
    query.with(new Sort(Direction.DESC, "updatedUtc"));
    return mongoTemplate.find(query, PersonSignIn.class);
  }
}
