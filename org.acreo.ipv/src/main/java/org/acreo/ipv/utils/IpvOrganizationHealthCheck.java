package org.acreo.ipv.utils;

import org.acreo.ip.service.OrganizationService;

import com.codahale.metrics.health.HealthCheck;

public class IpvOrganizationHealthCheck extends HealthCheck {
  private static final String HEALTHY = "The Organization Service is healthy for read and write";
  private static final String UNHEALTHY = "The Organization Service is not healthy. ";
  private static final String MESSAGE_PLACEHOLDER = "{}";

  private final OrganizationService organizationService ;

  public IpvOrganizationHealthCheck(OrganizationService organizationService ) {
    this.organizationService  = organizationService ;
  }

  @Override
  public Result check() throws Exception {
    String mySqlHealthStatus = organizationService.performHealthCheck();

    if (mySqlHealthStatus == null) {
      return Result.healthy(HEALTHY);
    } else {
      return Result.unhealthy(UNHEALTHY + MESSAGE_PLACEHOLDER, mySqlHealthStatus);
    }
  }
}
