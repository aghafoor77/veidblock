package org.acreo.ip.database;

import java.util.List;

import org.acreo.ip.entities.OTP;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(OTPMapper.class)
public interface OTPDao {
	
  @SqlQuery("select * from OTP;")
  public List<OTP> getAllOTPs();

  @SqlQuery("select * from OTP where resourceId = :resourceId;")
  public List<OTP> getAllOTPsByResource(@Bind("resourceId") final String resourceId);
  
  @SqlQuery("select * from OTP where resourceId = :resourceId AND otp = :otp AND expStatus = :expStatus")
  public OTP getOTPStatus(@Bind("resourceId") final String resourceId, @Bind("otp") final String otp, @Bind("expStatus") final int expStatus);

  @SqlUpdate("insert into "
  		+ "OTP (otp, resourceId, sentDate, expStatus) "
  		+ "values(:otp, :resourceId, :sentDate, :expStatus)")
  void createOTP(@BindBean final OTP otp);
  
  @SqlUpdate("update OTP set "
  		+ "expStatus = coalesce(:expStatus, expStatus) where resourceId = :resourceId AND otp = :otp" )
  void editOTP(@BindBean final OTP otp);

  @SqlUpdate("delete from OTP where resourceId = :resourceId AND expStatus = :expStatus")
  int deleteResource(@Bind("resourceId") final String resourceId, @Bind("expStatus") final int expStatus);
  
  @SqlQuery("select last_insert_id();")
  public String lastInsertId();
}