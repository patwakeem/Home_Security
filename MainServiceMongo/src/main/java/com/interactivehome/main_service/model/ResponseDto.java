package com.interactivehome.main_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDto {
  private boolean success;
  private String message;
}
