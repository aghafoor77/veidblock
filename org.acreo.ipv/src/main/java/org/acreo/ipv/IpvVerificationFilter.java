package org.acreo.ipv;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.acreo.common.entities.VeidErrorMessage;
import org.acreo.common.redirection.VerifierProxy;
import org.acreo.ip.entities.Resource;
import org.acreo.ip.service.ResourceService;
import org.acreo.veidblock.JWSToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dropwizard.auth.AuthFilter;

@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class IpvVerificationFilter extends AuthFilter {

	URI uri = null;
	private ResourceService resourceService;

	final static Logger logger = LoggerFactory.getLogger(ResourceService.class);

	public IpvVerificationFilter(URI uri, ResourceService resourceService) {
		this.uri = uri;
		this.resourceService = resourceService;
	}

	private boolean isSpecialUrl(String path, String method){

		String relaxUrl = "resource/user";
		String relaxmethod = "POST";
		if(method.equalsIgnoreCase(relaxmethod) && path.equalsIgnoreCase(relaxUrl)){
			return true;	
		}
		
		return false;
	}
	
	
	public void filter(ContainerRequestContext requestContext) throws IOException {
		try {
		
			VerifierProxy verifyerProxy = new VerifierProxy(uri);
			Object obj = verifyerProxy.process(requestContext);
			
			if(Objects.isNull(obj)){
				throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).entity("").build());
			}
			
			if(obj instanceof VeidErrorMessage){
				VeidErrorMessage veidErrorMessage = (VeidErrorMessage)obj; 
				throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).entity(veidErrorMessage).build());
				
			}
			
			String token = (String) obj;
			
			{
				JWSToken jwsToken = new JWSToken(token);
				if (jwsToken.verify()) {
					logger.info("Verifying Access token locally !");
					Resource resource = null;
					try {
						long rid = Long.parseLong(jwsToken.getPayload().getSub());
						resource = resourceService.getResource(rid);

					} catch (Exception exp) {

						resource = resourceService.getResource(jwsToken.getPayload().getSub());

					}

					if (resource == null) {
						if(isSpecialUrl(requestContext.getUriInfo().getPath(), requestContext.getRequest().getMethod())){
							return; 
						}
						VeidErrorMessage veidErrorMessage = new VeidErrorMessage(Response.Status.NOT_FOUND.getStatusCode(), "User/Resource not registered !"); 
						throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).entity(veidErrorMessage).build());
					}

					resourceService.getResource(jwsToken.getPayload().getSub());
					requestContext.getHeaders().remove(HttpHeaders.AUTHORIZATION);
					requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer" + " " + token);
					requestContext.getHeaders().add("RID", jwsToken.getPayload().getSub());
				} else {
					VeidErrorMessage veidErrorMessage = new VeidErrorMessage(Response.Status.UNAUTHORIZED.getStatusCode(), "Problems when handling credetnails !"); 
					throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).entity(veidErrorMessage).build());
				}
			}
		} catch (WebApplicationException e) {
			if(isSpecialUrl(requestContext.getUriInfo().getPath(), requestContext.getRequest().getMethod())){
				return;
			}
			throw e;
		} catch (URISyntaxException e) {
			if(isSpecialUrl(requestContext.getUriInfo().getPath(), requestContext.getRequest().getMethod())){
				return;
			}
			VeidErrorMessage veidErrorMessage = new VeidErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Internal Error. Try later or contact with services owner !"); 
			throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(veidErrorMessage).build());			
		} catch (Exception e) {
			if(isSpecialUrl(requestContext.getUriInfo().getPath(), requestContext.getRequest().getMethod())){
				return;
			}
			VeidErrorMessage veidErrorMessage = new VeidErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Internal Error. Try later or contact with services owner !"); 
			throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(veidErrorMessage).build());
		}
	}
}