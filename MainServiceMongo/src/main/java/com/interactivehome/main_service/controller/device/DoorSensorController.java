package com.interactivehome.main_service.controller.device;

import com.interactivehome.main_service.model.device.dto.DoorSensorDto;
import com.interactivehome.main_service.model.device.entity.DoorSensor;
import com.interactivehome.main_service.service.device.DoorSensorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class DoorSensorController {
    private final DoorSensorService doorSensorService;

    public DoorSensorController(DoorSensorService doorSensorService) {
        this.doorSensorService = doorSensorService;
    }

    @GetMapping("/door_sensor/{alarmId}/{sensorId}")
    public DoorSensor getDoorSensorByAlarmIdAndSensorId(
            @PathVariable Integer alarmId,
            @PathVariable Integer sensorId) {
        return doorSensorService.getDoorSensorByAlarmIdAndSensorId(alarmId, sensorId);
    }

    @PostMapping("/register_door_sensor")
    public void registerDoorSensor(@RequestBody DoorSensorDto dto) {
        doorSensorService.addDoorSensor(dto);
    }

    @PutMapping("/modify_door_sensor")
    public void modifyDoorSensor(@RequestBody DoorSensorDto dto) {
        doorSensorService.modifyDoorSensor(dto);
    }

    @DeleteMapping("/delete_door_sensor")
    public void deleteDoorSensor(@RequestBody DoorSensorDto dto) {
        doorSensorService.deleteDoorSensor(dto);
    }

    @GetMapping("door_sensors/{alarmId}")
    public List<DoorSensor> getDoorSensorsByAlarmId(@PathVariable Integer alarmId) {
        return doorSensorService.getAllDoorSensorsByAlarmId(alarmId);
    }
}
