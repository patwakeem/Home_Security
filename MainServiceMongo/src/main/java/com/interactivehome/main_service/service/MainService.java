package com.interactivehome.main_service.service;

import com.interactivehome.main_service.model.dto.*;
import com.interactivehome.main_service.model.entity.DoorSensor;
import com.interactivehome.main_service.repository.DoorStateRepository;
import com.interactivehome.main_service.repository.MainRepository;
import org.springframework.stereotype.Service;

@Service
public class MainService {
    private MainRepository mainRepository;
    private DoorStateRepository doorStateRepository;

    public MainService(MainRepository mainRepository, DoorStateRepository doorStateDocumentRepository) {
        this.mainRepository = mainRepository;
        this.doorStateRepository = doorStateDocumentRepository;
    }

    public String getAlarmState() { return mainRepository.getAlarmState(); }

    public void setAlarmState(AlarmDTO alarmDTO) {
        AlarmDTO dto = new AlarmDTO();
        dto.setAlarm_state(alarmDTO.getAlarm_state());
        mainRepository.storeAlarmState(dto);
    }

    public String getAlarmOn() { return mainRepository.getAlarmOn(); }

    public void setAlarmOn(AlarmDTO alarmDTO) {
        AlarmDTO dto = new AlarmDTO();
        dto.setAlarm_on(alarmDTO.getAlarm_on());
        mainRepository.storeAlarmOn(dto);
    }

    public void setDoorState(DoorSensorDTO doorSensorDTO) {
        DoorSensorDTO dto = new DoorSensorDTO();
        dto.setDoorState(doorSensorDTO.getDoorState());
        DoorSensor doorSensorStateDocument = new DoorSensor();
        doorSensorStateDocument.setState(doorSensorDTO.getDoorState());
        doorStateRepository.save(doorSensorStateDocument);
//        mainRepository.storeDoorState(dto);
    }

    public void setGasValue(SmokeSensorDTO smokeSensorDTO) {
        SmokeSensorDTO dto = new SmokeSensorDTO();
        dto.setGasValue(smokeSensorDTO.getGasValue());
        mainRepository.storeGasValue(dto);
    }

    public void setTemperature(ThermometerSensorDTO thermometerSensorDTO) {
        ThermometerSensorDTO dto = new ThermometerSensorDTO();
        dto.setTemperature(thermometerSensorDTO.getTemperature());
        mainRepository.storeTemperature(dto);
    }

    public String getTemperature() {
        return mainRepository.getTemperature();
    }

    public void setSensorsValues(SensorsDTO sensorsDTO) {
        SensorsDTO dto = new SensorsDTO();
        dto.setTemperature(sensorsDTO.getTemperature());
        dto.setHumidity(sensorsDTO.getHumidity());
        dto.setGasValue(sensorsDTO.getGasValue());

        ThermometerSensorDTO thermometerSensorDTO = new ThermometerSensorDTO();
        thermometerSensorDTO.setTemperature(dto.getTemperature());
        HumiditySensorDTO humiditySensorDTO = new HumiditySensorDTO();
        humiditySensorDTO.setHumidity(dto.getHumidity());
        SmokeSensorDTO smokeSensorDTO = new SmokeSensorDTO();
        smokeSensorDTO.setGasValue(dto.getGasValue());
        mainRepository.storeTemperature(thermometerSensorDTO);
        mainRepository.storeHumidityValue(humiditySensorDTO);
        mainRepository.storeGasValue(smokeSensorDTO);
    }

    public void setPersonVerified(PersonVerifiedDTO personVerifiedDTO) {
        PersonVerifiedDTO dto = new PersonVerifiedDTO();
        dto.setPerson_verified(personVerifiedDTO.getPerson_verified());
        mainRepository.storePersonVerified(dto);
    }

    public void setVerifyPassword(VerifyPasswordDTO verifyPasswordDTO) {
        VerifyPasswordDTO dto = new VerifyPasswordDTO();
        dto.setPassword(verifyPasswordDTO.getPassword());
    }

}
