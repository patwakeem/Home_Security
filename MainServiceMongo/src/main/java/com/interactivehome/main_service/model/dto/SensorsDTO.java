package com.interactivehome.main_service.model.dto;

import lombok.Data;

@Data
public class SensorsDTO {
    private Float temperature;
    private Integer humidity;
    private Integer gasValue;
}
