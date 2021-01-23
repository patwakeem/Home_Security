package com.interactivehome.main_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("iot")
@Data
public class AppProperties {

//  Try to keep your fields private by default.
//  You have the @Data annotation so you have get methods for everything.
  public String securityControllerIpPort;
  public String verificationProcessEndpoint;
  public Integer verificationProcessTimeoutSec;
  public String alarmStateEndpoint;
  public String stopAlarmEndpoint;
}
