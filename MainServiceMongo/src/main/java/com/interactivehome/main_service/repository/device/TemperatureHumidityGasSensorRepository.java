package com.interactivehome.main_service.repository.device;

import com.interactivehome.main_service.model.device.entity.TemperatureHumidityGasSensor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemperatureHumidityGasSensorRepository extends MongoRepository<TemperatureHumidityGasSensor, String> {
}
