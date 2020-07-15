package com.interactivehome.main_service.service;

import com.interactivehome.main_service.model.dto.BatteryDto;
import com.interactivehome.main_service.model.entity.Battery;
import com.interactivehome.main_service.repository.BatteryRepository;
import org.springframework.stereotype.Service;

@Service
public class BatteryServiceImpl implements BatteryService {
  private final BatteryRepository batteryRepository;

  public BatteryServiceImpl(BatteryRepository batteryRepository) {
    this.batteryRepository = batteryRepository;
  }

  @Override
  public void saveVoltage(BatteryDto dto) {
    Battery battery = new Battery();
    battery.mapFromDto(dto);
    batteryRepository.save(battery);
  }

  @Override
  public BatteryDto getVoltageByBatteryId(Integer batteryId) {
    return null;
  }
}
