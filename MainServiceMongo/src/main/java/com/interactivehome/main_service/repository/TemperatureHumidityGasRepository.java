package com.interactivehome.main_service.repository;

import com.interactivehome.main_service.model.entity.TemperatureHumidityGas;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemperatureHumidityGasRepository extends MongoRepository<TemperatureHumidityGas, String> {

}
