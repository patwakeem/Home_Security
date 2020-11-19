package com.interactivehome.main_service.controller.device;

import com.interactivehome.main_service.model.device.dto.EnvironmentSensorDto;
import com.interactivehome.main_service.model.device.entity.EnvironmentSensor;
import com.interactivehome.main_service.service.device.EnvironmentSensorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "*")
@RestController
public class EnvironmentSensorController {

    private final EnvironmentSensorService environmentSensorService;

    public EnvironmentSensorController(EnvironmentSensorService environmentSensorService) {
        this.environmentSensorService = environmentSensorService;
    }

    @PostMapping("/environment_sensor")
    public void registerEnvironmentSensor(@RequestBody EnvironmentSensorDto dto) {
        environmentSensorService.registerEnvironmentSensor(dto);
    }

    @PutMapping("/environment_sensor/{alarmId}/{id}")
    public void modifyEnvironmentSensorByAlarmIdAndId(@PathVariable Integer alarmId,
                                                      @PathVariable Integer id,
                                                      @RequestBody EnvironmentSensorDto dto) {
        environmentSensorService.modifyEnvironmentSensorByAlarmIdAndId(alarmId, id, dto);
    }

    @GetMapping("/environment_sensor/{alarmId}/{id}")
    public EnvironmentSensor getEnvironmentSensorByAlarmIdAndId(@PathVariable Integer alarmId,
                                                                @PathVariable Integer id) {
        return environmentSensorService.getEnvironmentSensorByAlarmIdAndId(alarmId, id);
    }

    @GetMapping("/environment_sensor/{alarmId}")
    public List<EnvironmentSensor> getAllEnvironmentSensorsByAlarmId(@PathVariable Integer alarmId) {
        return environmentSensorService.getAllEnvironmentSensorsByAlarmId(alarmId);
    }

    @DeleteMapping("/environment_sensor/{alarmId}/{id}")
    public void deleteEnvironmentSensorByAlarmIdAndId(
            @PathVariable Integer alarmId,
            @PathVariable Integer id) {
        environmentSensorService.deleteEnvironmentSensorByAlarmIdAndId(alarmId, id);
    }
}
