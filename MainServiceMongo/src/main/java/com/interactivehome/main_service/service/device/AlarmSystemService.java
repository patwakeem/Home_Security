package com.interactivehome.main_service.service.device;

import com.interactivehome.main_service.model.device.dto.AlarmSystemDto;
import com.interactivehome.main_service.model.device.entity.AlarmSystem;

import java.util.List;

public interface AlarmSystemService {
    void addAlarmSystem(AlarmSystemDto dto);
    Integer getAlarmSystemNewId();
    void deleteAlarmSystem(AlarmSystemDto dto);
    void modifyAlarmSystem(AlarmSystemDto dto);
    void setActiveAlarmSystem(AlarmSystemDto dto);
    AlarmSystem getActiveAlarmSystem();
    List<AlarmSystem> getAllAlarmSystems();
}
