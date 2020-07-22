package com.interactivehome.main_service.service;

import com.interactivehome.main_service.model.dto.PersonSignInDto;
import com.interactivehome.main_service.model.dto.RegisteredPersonDto;
import com.interactivehome.main_service.model.entity.RegisteredPerson;
import com.interactivehome.main_service.repository.RegisteredPersonRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
  public List<RegisteredPerson> getAllRegisteredPersons() {
    return mongoTemplate.findAll(RegisteredPerson.class);
  }

  @Override
  public RegisteredPerson getRegisteredPersonByPassword(String password) {
    String name = "";
    Query query = new Query();
    query.addCriteria(Criteria.where("password").is(password));
    RegisteredPerson registeredPerson = new RegisteredPerson();
    if(mongoTemplate.findOne(query, RegisteredPerson.class) != null)
    {
      registeredPerson = mongoTemplate.findOne(query, RegisteredPerson.class);
    }
    return registeredPerson;
  }

  @Override
  public RegisteredPerson getRegisteredPersonByRfidCardId(String rfidCardId) {
    String name = "";
    Query query = new Query();
    query.addCriteria(Criteria.where("rfidCardId").is(rfidCardId));
    RegisteredPerson registeredPerson = new RegisteredPerson();
    if(mongoTemplate.findOne(query, RegisteredPerson.class) != null)
    {
      registeredPerson = mongoTemplate.findOne(query, RegisteredPerson.class);
    }
    return registeredPerson;
  }

  @Override
  public String getRegisteredPersonNameByRfidCardId(String rfidCardId) {
    String name = "";
    Query query = new Query();
    query.addCriteria(Criteria.where("rfidCardId").is(rfidCardId));
    if(mongoTemplate.findOne(query, RegisteredPerson.class) != null)
    {
      RegisteredPerson registeredPerson = mongoTemplate.findOne(query, RegisteredPerson.class);
      name = registeredPerson.getName();

      // Store the signing in of the person to the database
      PersonSignInDto personSignInDto = new PersonSignInDto();
      personSignInDto.setRfidCardId(registeredPerson.getRfidCardId());
      personSignInDto.setRfidCardIdSigned(false);
      personSignInService.savePersonSingIn(personSignInDto);
    }
    return name;
  }

  @Override
  public String getRegisteredNamePersonByPassword(String password) {
    String name = "";
    Query query = new Query();
    query.addCriteria(Criteria.where("password").is(password));
    if(mongoTemplate.findOne(query, RegisteredPerson.class) != null)
    {
      RegisteredPerson registeredPerson = mongoTemplate.findOne(query, RegisteredPerson.class);
      name = registeredPerson.getName();

      // Store the signing in of the person to the database
      PersonSignInDto personSignInDto = new PersonSignInDto();
      personSignInDto.setRfidCardId(registeredPerson.getRfidCardId());
      personSignInDto.setRfidCardIdSigned(false);
      personSignInService.savePersonSingIn(personSignInDto);
    }
    return name;
  }
}
