package org.acreo.ipv.utils;

import org.acreo.ip.service.RoleService;

import com.codahale.metrics.health.HealthCheck;

public class IpvRoleHealthCheck extends HealthCheck {
  private static final String HEALTHY = "The Role Service is healthy for read and write";
  private static final String UNHEALTHY = "The Role Service is not healthy. ";
  private static final String MESSAGE_PLACEHOLDER = "{}";

  private final RoleService roleService ;

  public IpvRoleHealthCheck(RoleService roleService ) {
    this.roleService  = roleService ;
  }

  @Override
  public Result check() throws Exception {
    String mySqlHealthStatus = roleService.performHealthCheck();

    if (mySqlHealthStatus == null) {
      return Result.healthy(HEALTHY);
    } else {
      return Result.unhealthy(UNHEALTHY + MESSAGE_PLACEHOLDER, mySqlHealthStatus);
    }
  }
}
