package com.interactivehome.main_service.controller.device;

import com.interactivehome.main_service.model.device.dto.AlarmSystemDto;
import com.interactivehome.main_service.model.device.entity.AlarmSystem;
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

    @PostMapping("/alarm_system")
    public void registerAlarmSystem(@RequestBody AlarmSystemDto dto) {
        alarmSystemService.registerAlarmSystem(dto);
    }

    @PutMapping("/alarm_system/{id}")
    public AlarmSystem modifyAlarmSystem(@PathVariable Integer id,
                                  @RequestBody AlarmSystemDto dto) {
        return alarmSystemService.modifyAlarmSystemById(id, dto);
    }

    @GetMapping("alarm_system/{id}")
    public AlarmSystem getAlarmSystemById(@PathVariable Integer id) {
        return alarmSystemService.getAlarmSystemById(id);
    }

    @GetMapping("alarm_system")
    public List<AlarmSystem> getAllAlarmSystems() {
        return alarmSystemService.getAllAlarmSystems();
    }

    @DeleteMapping("/alarm_system/{id}")
    public void deleteAlarmSystem(@PathVariable Integer id) {
        alarmSystemService.deleteAlarmSystem(id);
    }
}
