package com.interactivehome.main_service.controller.device;

import com.interactivehome.main_service.model.device.dto.MovementSensorDto;
import com.interactivehome.main_service.model.device.entity.MovementSensor;
import com.interactivehome.main_service.service.device.MovementSensorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class MovementSensorController {

    private final MovementSensorService movementSensorService;

    public MovementSensorController(MovementSensorService movementSensorService) {
        this.movementSensorService = movementSensorService;
    }

    @PostMapping("/register_movement_sensor")
    public void registerMovementSensor(@RequestBody MovementSensorDto dto) {
        movementSensorService.addMovementSensor(dto);
    }

    @GetMapping("/movement_sensor/{alarmId}/{sensorId}")
    public MovementSensor getSensorByAlarmIdAndSensorId(
            @PathVariable Integer alarmId,
            @PathVariable Integer sensorId) {
        return movementSensorService.getSensorByAlarmIdAndSensorId(alarmId, sensorId);
    }

    @PutMapping("/modify_movement_sensor")
    public void modifyMovementSensor(@RequestBody MovementSensorDto dto) {
        movementSensorService.modifyMovementSensor(dto);
    }

    @DeleteMapping("/delete_movement_sensor")
    public void deleteMovementSensor(@RequestBody MovementSensorDto dto) {
        movementSensorService.deleteMovementSensor(dto);
    }

    @GetMapping("/movement_sensors/{alarmId}")
    public List<MovementSensor> getAllMovementSensorsByAlarmId(@PathVariable Integer alarmId) {
        return movementSensorService.getAllMovementSensorsByAlarmId(alarmId);
    }
}
