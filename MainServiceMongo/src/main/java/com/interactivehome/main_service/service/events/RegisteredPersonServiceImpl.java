package com.interactivehome.main_service.service.events;

import com.interactivehome.main_service.model.events.dto.PersonSignInDto;
import com.interactivehome.main_service.model.events.dto.RegisteredPersonDto;
import com.interactivehome.main_service.model.events.entity.RegisteredPerson;
import com.interactivehome.main_service.repository.events.RegisteredPersonRepository;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class RegisteredPersonServiceImpl implements RegisteredPersonService {

  @Autowired
  PersonSignInService personSignInService;

  private final RegisteredPersonRepository registeredPersonRepository;
  private final MongoTemplate mongoTemplate;


  RegisteredPersonServiceImpl(RegisteredPersonRepository registeredPersonRepository,
                              MongoTemplate mongoTemplate) {
    this.registeredPersonRepository = registeredPersonRepository;
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public void savePerson(RegisteredPersonDto dto) {
    RegisteredPerson registeredPerson = new RegisteredPerson();
    registeredPerson.mapFromDto(dto);
    mongoTemplate.save(registeredPerson);
  }

  @Override
  public List<RegisteredPerson> getAllRegisteredPersonsByAlarmId(Integer alarmId) {
    Query query = new Query();
    query.addCriteria(Criteria.where("alarm_id").is(alarmId));
    query.with(new org.springframework.data.domain.Sort(Sort.Direction.DESC, "updated_utc"));

    return mongoTemplate.find(query, RegisteredPerson.class);
  }

  @Override
  public RegisteredPerson getRegisteredPersonByAlarmIdAndPassword(Integer alarmId, String password) {
    String name = "";
    Query query = new Query();
    query.addCriteria(Criteria.where("alarm_id").is(alarmId));
    query.addCriteria(Criteria.where("password").is(password));
    RegisteredPerson registeredPerson = new RegisteredPerson();
    if(mongoTemplate.findOne(query, RegisteredPerson.class) != null)
    {
      registeredPerson = mongoTemplate.findOne(query, RegisteredPerson.class);
    }
    return registeredPerson;
  }

  @Override
  public RegisteredPerson getRegisteredPersonByAlarmIdAndRfidCardId(Integer alarmId, String rfidCardId) {
    Query query = new Query();
    query.addCriteria(Criteria.where("alarm_id").is(alarmId));
    query.addCriteria(Criteria.where("rfid_card_id").is(rfidCardId));
    RegisteredPerson registeredPerson = new RegisteredPerson();
    if(mongoTemplate.findOne(query, RegisteredPerson.class) != null)
    {
      registeredPerson = mongoTemplate.findOne(query, RegisteredPerson.class);
    }
    return registeredPerson;
  }

  @Override
  public String getRegisteredPersonNameByAlarmIdAndRfidCardId(Integer alarmId, String rfidCardId) {
    String name = "";
    Query query = new Query();
    query.addCriteria(Criteria.where("alarm_id").is(alarmId));
    query.addCriteria(Criteria.where("rfid_card_id").is(rfidCardId));
    if(mongoTemplate.findOne(query, RegisteredPerson.class) != null)
    {
      RegisteredPerson registeredPerson = mongoTemplate.findOne(query, RegisteredPerson.class);
      name = registeredPerson != null ? registeredPerson.getName() : "";

      // Store the signing in of the person to the database
      PersonSignInDto personSignInDto = new PersonSignInDto();
      personSignInDto.setRfidCardId(registeredPerson.getRfidCardId());
      personSignInDto.setRfidCardIdSigned(false);
      personSignInService.savePersonSingIn(personSignInDto);
    }
    return name;
  }

  @Override
  public String getRegisteredNamePersonByAlarmIdAndPassword(Integer alarmId, String password) {
    String name = "";
    Query query = new Query();
    query.addCriteria(Criteria.where("alarm_id").is(alarmId));
    query.addCriteria(Criteria.where("password").is(password));
    if(mongoTemplate.findOne(query, RegisteredPerson.class) != null)
    {
      RegisteredPerson registeredPerson = mongoTemplate.findOne(query, RegisteredPerson.class);
      name = Objects.requireNonNull(registeredPerson).getName() != null ? registeredPerson.getName() : "";

      // Store the signing in of the person to the database
      PersonSignInDto personSignInDto = new PersonSignInDto();
      personSignInDto.setRfidCardId(registeredPerson.getRfidCardId());
      personSignInDto.setRfidCardIdSigned(false);
      personSignInService.savePersonSingIn(personSignInDto);
    }
    return name;
  }
}
