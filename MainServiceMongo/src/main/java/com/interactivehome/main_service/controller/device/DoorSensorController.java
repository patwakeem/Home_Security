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

    @PostMapping("/door_sensor")
    public void registerDoorSensor(@RequestBody DoorSensorDto dto) {
        doorSensorService.registerDoorSensor(dto);
    }

    @PutMapping("/door_sensor/{alarmId}/{id}")
    public DoorSensor modifyDoorSensorByAlarmIdAndId(@PathVariable Integer alarmId,
                                                     @PathVariable Integer id,
                                                     @RequestBody DoorSensorDto dto) {
        return doorSensorService.modifyDoorSensorByAlarmIdAndId(alarmId, id, dto);
    }

    @GetMapping("/door_sensor/{alarmId}/{id}")
    public DoorSensor getDoorSensorByAlarmIdAndId(
            @PathVariable Integer alarmId,
            @PathVariable Integer id) {
        return doorSensorService.getDoorSensorByAlarmIdAndId(alarmId, id);
    }

    @GetMapping("door_sensor/{alarmId}")
    public List<DoorSensor> getAllDoorSensorsByAlarmId(@PathVariable Integer alarmId) {
        return doorSensorService.getAllDoorSensorsByAlarmId(alarmId);
    }

    @DeleteMapping("/door_sensor/{alarmId}/{id}")
    public void deleteDoorSensorByAlarmIdAndId(@PathVariable Integer alarmId,
                                     @PathVariable Integer id) {
        doorSensorService.deleteDoorSensorByAlarmIdAndId(alarmId, id);
    }
}
