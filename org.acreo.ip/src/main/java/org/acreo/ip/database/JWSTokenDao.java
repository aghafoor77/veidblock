package org.acreo.ip.database;

import java.util.Date;
import java.util.List;

import org.acreo.ip.entities.JwsTokenDb;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;


@RegisterMapper(JWSTokenMapper.class)
public interface JWSTokenDao {
	
  @SqlQuery("select * from JwsToken;")
  public List<JwsTokenDb> getJwsTokens();

  @SqlQuery("select * from JwsToken where jti = :jti")
  public JwsTokenDb getJwsToken(@Bind("jti") final long jti);
	
  @SqlUpdate("insert into JwsToken(jti, alg, type, iss, sub, ver, exp, pub,scp, refreshToken, signature, signerData, signerDataType) values(:jti, :alg, :type, :iss, :sub, :ver, :exp, :pub, :scp, :refreshToken, :signature, :signerData, :signerDataType)")
  void createJwsToken(@BindBean final JwsTokenDb jwsTokenDb);
  
  @SqlQuery("select * from JwsToken where sub = :sub AND exp > :exp")
  public JwsTokenDb getJwsToken(@Bind("sub") final String sub, @Bind("exp") final Date exp);

  @SqlUpdate("delete from JwsToken where jti = :jti")
  int deleteJwsToken(@Bind("jti") final long jti);
  
  @SqlQuery("select last_insert_id();")
  public int lastInsertId();
  
}




