package com.interactivehome.main_service.service;

import com.interactivehome.main_service.model.dto.RegisteredPersonDto;
import com.interactivehome.main_service.model.entity.RegisteredPerson;
import com.interactivehome.main_service.repository.RegisteredPersonRepository;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class RegisteredPersonServiceImpl implements RegisteredPersonService {

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
  public RegisteredPerson getRegisteredPersonByRfidCardId(String rfidCardId) {
    Query query = new Query();
    query.addCriteria(Criteria.where("rfidCardId").is(rfidCardId));
    return mongoTemplate.findOne(query, RegisteredPerson.class);
  }

  @Override
  public RegisteredPerson getRegisteredPersonByPassword(String password) {
    Query query = new Query();
    query.addCriteria(Criteria.where("password").is(password));
    return mongoTemplate.findOne(query, RegisteredPerson.class);
  }
}
