package com.interactivehome.main_service.controller.device;

import com.interactivehome.main_service.model.device.dto.AlarmSystemDto;
import com.interactivehome.main_service.model.device.entity.AlarmSystem;
import com.interactivehome.main_service.model.device.entity.DoorSensor;
import com.interactivehome.main_service.service.device.AlarmSystemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class AlarmSystemController {
    private final AlarmSystemService alarmSystemService;

    public AlarmSystemController(AlarmSystemService alarmSystemService) {
        this.alarmSystemService = alarmSystemService;
    }

    @GetMapping("/active_alarm_system")
    public AlarmSystem getDefaultAlarmSystem() {
        return alarmSystemService.getActiveAlarmSystem();
    }

    @PostMapping("/register_alarm_system")
    public void registerAlarmSystem(@RequestBody AlarmSystemDto dto) {
        alarmSystemService.addAlarmSystem(dto);
    }

    @PutMapping("/modify_alarm_system")
    public void modifyAlarmSystem(@RequestBody AlarmSystemDto dto) {
        alarmSystemService.modifyAlarmSystem(dto);
    }

    @PutMapping("/set_active_alarm_system")
    public void setActiveAlarmSystem(@RequestBody AlarmSystemDto dto) {
        alarmSystemService.setActiveAlarmSystem(dto);
    }

    @DeleteMapping("/delete_alarm_system")
    public void deleteAlarmSystem(@RequestBody AlarmSystemDto dto) {
        alarmSystemService.deleteAlarmSystem(dto);
    }

    @GetMapping("alarm_systems")
    public List<AlarmSystem> getAllAlarmSystems() {
        return alarmSystemService.getAllAlarmSystems();
    }
}
