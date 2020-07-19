package com.interactivehome.main_service.service;


import com.interactivehome.main_service.model.dto.DoorSensorDto;
import com.interactivehome.main_service.model.entity.DoorSensor;
import java.util.Date;
import java.util.List;

public interface DoorStateService {
  void saveState(DoorSensorDto dto);

  List<DoorSensor> getDoorStateByDoorIdFromDateToDate(Integer doorId, Date fromDate, Date toDate);
}
