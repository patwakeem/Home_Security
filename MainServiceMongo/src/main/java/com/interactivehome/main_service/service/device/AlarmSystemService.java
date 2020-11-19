package com.interactivehome.main_service.service.device;

import com.interactivehome.main_service.model.device.dto.AlarmSystemDto;
import com.interactivehome.main_service.model.device.entity.AlarmSystem;

import java.util.List;

public interface AlarmSystemService {
    void registerAlarmSystem(AlarmSystemDto dto);
    AlarmSystem modifyAlarmSystemById(Integer id, AlarmSystemDto dto);
    AlarmSystem getAlarmSystemById(Integer id);
    List<AlarmSystem> getAllAlarmSystems();
    void deleteAlarmSystem(Integer id);
}
