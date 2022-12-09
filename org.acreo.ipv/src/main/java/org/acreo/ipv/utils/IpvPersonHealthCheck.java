package org.acreo.ipv.utils;

import org.acreo.ip.service.ResourceService;

import com.codahale.metrics.health.HealthCheck;

public class IpvPersonHealthCheck extends HealthCheck {
  private static final String HEALTHY = "The Person Service is healthy for read and write";
  private static final String UNHEALTHY = "The Person Service is not healthy. ";
  private static final String MESSAGE_PLACEHOLDER = "{}";

  private final ResourceService personService ;

  public IpvPersonHealthCheck(ResourceService personService ) {
    this.personService  = personService ;
  }

  @Override
  public Result check() throws Exception {
    String mySqlHealthStatus = personService.performHealthCheck();

    if (mySqlHealthStatus == null) {
      return Result.healthy(HEALTHY);
    } else {
      return Result.unhealthy(UNHEALTHY + MESSAGE_PLACEHOLDER, mySqlHealthStatus);
    }
  }
}
