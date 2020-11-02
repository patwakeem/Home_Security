package com.interactivehome.main_service.controller.device;

import com.interactivehome.main_service.model.device.dto.TemperatureHumidityGasSensorDto;
import com.interactivehome.main_service.model.device.entity.TemperatureHumidityGasSensor;
import com.interactivehome.main_service.service.device.TemperatureHumidityGasSensorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "*")
@RestController
public class TemperatureHumidityGasSensorController {

    private final TemperatureHumidityGasSensorService temperatureHumidityGasSensorService;

    public TemperatureHumidityGasSensorController(TemperatureHumidityGasSensorService temperatureHumidityGasSensorService) {
        this.temperatureHumidityGasSensorService = temperatureHumidityGasSensorService;
    }

    @PostMapping("/register_t_h_g_sensor")
    public void registerTemperatureHumidityGasSensor(@RequestBody TemperatureHumidityGasSensorDto dto) {
        temperatureHumidityGasSensorService.addTemperatureHumidityGasSensor(dto);
    }

    @GetMapping("/t_h_g_sensor/{alarmId}/{sensorId}")
    public TemperatureHumidityGasSensor getTemperatureHumidityGasSensorByAlarmIdAndSensorId(
            @PathVariable Integer alarmId,
            @PathVariable Integer sensorId) {
        return temperatureHumidityGasSensorService.getTemperatureHumidityGasSensorByAlarmIdAndSensorId(alarmId, sensorId);
    }

    @PutMapping("/modify_t_h_g_sensor")
    public void modifyTemperatureHumidityGasSensor(@RequestBody TemperatureHumidityGasSensorDto dto) {
        temperatureHumidityGasSensorService.modifyTemperatureHumidityGasSensor(dto);
    }

    @DeleteMapping("/delete_t_h_g_sensor/{alarmId}/{sensorId}")
    public void deleteTemperatureHumidityGasSensor(
            @PathVariable Integer alarmId,
            @PathVariable Integer sensorId) {
        temperatureHumidityGasSensorService.deleteTemperatureHumidityGasSensor(alarmId, sensorId);
    }

    @GetMapping("/t_h_g_sensors/{alarmId}")
    public List<TemperatureHumidityGasSensor> getAllTemperatureHumidityGasSensorsByAlarmId(@PathVariable Integer alarmId) {
        return temperatureHumidityGasSensorService.getAllTemperatureHumidityGasSensorsByAlarmId(alarmId);
    }
}
