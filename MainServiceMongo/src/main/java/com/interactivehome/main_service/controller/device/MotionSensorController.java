package com.interactivehome.main_service.controller.device;

import com.interactivehome.main_service.model.device.dto.MotionSensorDto;
import com.interactivehome.main_service.model.device.entity.MotionSensor;
import com.interactivehome.main_service.service.device.MotionSensorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class MotionSensorController {

    private final MotionSensorService motionSensorService;

    public MotionSensorController(MotionSensorService motionSensorService) {
        this.motionSensorService = motionSensorService;
    }

    @PostMapping("/motion_sensor")
    public void registerMotionSensor(@RequestBody MotionSensorDto dto) {
        motionSensorService.registerMotionSensor(dto);
    }

    @PutMapping("/motion_sensor/{alarmId}/{id}")
    public MotionSensor modifyMotionSensorByAlarmIdAndId(@PathVariable Integer alarmId,
                                           @PathVariable Integer id,
                                           @RequestBody MotionSensorDto dto) {
        return motionSensorService.modifyMotionSensorByAlarmIdAndId(alarmId, id, dto);
    }

    @GetMapping("/motion_sensor/{alarmId}/{id}")
    public MotionSensor getMotionSensorByAlarmIdAndId(@PathVariable Integer alarmId,
                                                      @PathVariable Integer id) {
        return motionSensorService.getMotionSensorByAlarmIdAndId(alarmId, id);
    }

    @GetMapping("/motion_sensor/{alarmId}")
    public List<MotionSensor> getAllMotionSensorsByAlarmId(@PathVariable Integer alarmId) {
        return motionSensorService.getAllMotionSensorsByAlarmId(alarmId);
    }

    @DeleteMapping("/motion_sensor/{alarmId}/{id}")
    public void deleteMotionSensorByAlarmIdAndId(@PathVariable Integer alarmId,
                                                 @PathVariable Integer id) {
        motionSensorService.deleteMotionSensor(alarmId, id);
    }
}
