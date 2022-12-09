package org.acreo.ipv.utils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;

import org.acreo.ip.entities.Resource;
import org.acreo.ip.entities.Role;
import org.acreo.ip.service.RoleService;

public class EnforceAuthorization {
	private RoleService roleService;

	public EnforceAuthorization(RoleService roleService) {
		this.roleService = roleService;
	}

	private int accessLevel = 1;

	public boolean isUser(String resourceId) {
		List<Role> allRoles = this.roleService.getRoles(Long.parseLong(resourceId));
		if(Objects.isNull(allRoles)){
			return true;
		}
		
		for (Role role : allRoles) {
			if (role.getRole().equalsIgnoreCase("user")) {
				if (accessLevel <= 1)
					accessLevel = 1;
			}
			if (role.getRole().equalsIgnoreCase("admin")) {
				if (accessLevel <= 2)
					accessLevel = 2;
			}
			if (role.getRole().equalsIgnoreCase("super")) {
				if (accessLevel <= 3)
					accessLevel = 3;
			}
		}
		if (accessLevel == 1)
			return true;
		else
			return false;
	}
	
	public boolean isAdmin(String resourceId) {
		List<Role> allRoles = this.roleService.getRoles(Long.parseLong(resourceId));
		if(Objects.isNull(allRoles)){
			return false;
		}
		for (Role role : allRoles) {
			if (role.getRole().equalsIgnoreCase("user")) {
				if (accessLevel <= 1)
					accessLevel = 1;
			}
			if (role.getRole().equalsIgnoreCase("admin")) {
				if (accessLevel <= 2)
					accessLevel = 2;
			}
			if (role.getRole().equalsIgnoreCase("super")) {
				if (accessLevel <= 3)
					accessLevel = 3;
			}
		}
		if (accessLevel == 2)
			return true;
		else
			return false;
	}
	public boolean isSupper(String resourceId) {
		List<Role> allRoles = this.roleService.getRoles(Long.parseLong(resourceId));
		if(Objects.isNull(allRoles)){
			return false;
		}
		for (Role role : allRoles) {
			if (role.getRole().equalsIgnoreCase("user")) {
				if (accessLevel <= 1)
					accessLevel = 1;
			}
			if (role.getRole().equalsIgnoreCase("admin")) {
				if (accessLevel <= 2)
					accessLevel = 2;
			}
			if (role.getRole().equalsIgnoreCase("super")) {
				if (accessLevel <= 3)
					accessLevel = 3;
			}
		}
		if (accessLevel == 3)
			return true;
		else
			return false;
	}

	public Hashtable<String, String> urls = new Hashtable<>();

	public enum HTTPMethod {
		GET, POST, PUT, DELETE
	};

	private class AuthorizationPolicyList extends ArrayList<AuthorizationPolicy> {
		public AuthorizationPolicyList() {
			List<Role> allRoles = new ArrayList<Role>();
			AuthorizationPolicy authorizationPolicy;
			authorizationPolicy = new AuthorizationPolicy(HTTPMethod.GET, "/organization", allRoles);
			add(authorizationPolicy);
			authorizationPolicy = new AuthorizationPolicy(HTTPMethod.POST, "/organization", allRoles);
			add(authorizationPolicy);
			authorizationPolicy = new AuthorizationPolicy(HTTPMethod.DELETE, "/organization/{id}", allRoles);
			add(authorizationPolicy);
			authorizationPolicy = new AuthorizationPolicy(HTTPMethod.GET, "/organization/{id}", allRoles);
			add(authorizationPolicy);
			authorizationPolicy = new AuthorizationPolicy(HTTPMethod.PUT, "/organization/{id}", allRoles);
			add(authorizationPolicy);
			authorizationPolicy = new AuthorizationPolicy(HTTPMethod.GET, "/resource", allRoles);
			add(authorizationPolicy);
			authorizationPolicy = new AuthorizationPolicy(HTTPMethod.GET, "/resource/server", allRoles);
			add(authorizationPolicy);
			authorizationPolicy = new AuthorizationPolicy(HTTPMethod.POST, "/resource/server", allRoles);
			add(authorizationPolicy);
			authorizationPolicy = new AuthorizationPolicy(HTTPMethod.DELETE, "/resource/server/id/{identity}",
					allRoles);
			add(authorizationPolicy);
			authorizationPolicy = new AuthorizationPolicy(HTTPMethod.GET, "/resource/server/id/{identity}", allRoles);
			add(authorizationPolicy);
			authorizationPolicy = new AuthorizationPolicy(HTTPMethod.PUT, "/resource/server/id/{identity}", allRoles);
			add(authorizationPolicy);
			authorizationPolicy = new AuthorizationPolicy(HTTPMethod.DELETE, "/resource/server/resname/{identity}",
					allRoles);
			add(authorizationPolicy);
			authorizationPolicy = new AuthorizationPolicy(HTTPMethod.GET, "/resource/server/resname/{identity}",
					allRoles);
			add(authorizationPolicy);
			authorizationPolicy = new AuthorizationPolicy(HTTPMethod.PUT, "/resource/server/resname/{identity}",
					allRoles);
			add(authorizationPolicy);
			authorizationPolicy = new AuthorizationPolicy(HTTPMethod.GET, "/resource/user", allRoles);
			add(authorizationPolicy);
			authorizationPolicy = new AuthorizationPolicy(HTTPMethod.POST, "/resource/user", allRoles);
			add(authorizationPolicy);
			authorizationPolicy = new AuthorizationPolicy(HTTPMethod.DELETE, "/resource/user/id/{identity}", allRoles);
			add(authorizationPolicy);
			authorizationPolicy = new AuthorizationPolicy(HTTPMethod.GET, "/resource/user/id/{identity}", allRoles);
			add(authorizationPolicy);
			authorizationPolicy = new AuthorizationPolicy(HTTPMethod.PUT, "/resource/user/id/{identity}", allRoles);
			add(authorizationPolicy);
			authorizationPolicy = new AuthorizationPolicy(HTTPMethod.DELETE, "/resource/user/resname/{identity}",
					allRoles);
			add(authorizationPolicy);
			authorizationPolicy = new AuthorizationPolicy(HTTPMethod.GET, "/resource/user/resname/{identity}",
					allRoles);
			add(authorizationPolicy);
			authorizationPolicy = new AuthorizationPolicy(HTTPMethod.PUT, "/resource/user/resname/{identity}",
					allRoles);
			add(authorizationPolicy);
			authorizationPolicy = new AuthorizationPolicy(HTTPMethod.GET, "/role", allRoles);
			add(authorizationPolicy);
			authorizationPolicy = new AuthorizationPolicy(HTTPMethod.POST, "/role", allRoles);
			add(authorizationPolicy);
			authorizationPolicy = new AuthorizationPolicy(HTTPMethod.GET, "/role/personid/{personId}", allRoles);
			add(authorizationPolicy);
			authorizationPolicy = new AuthorizationPolicy(HTTPMethod.DELETE,
					"/role/resourceid/{resourceId}/role/{role}", allRoles);
			add(authorizationPolicy);
			authorizationPolicy = new AuthorizationPolicy(HTTPMethod.POST, "/role/resourceid/{resourceId}/role/{role}",
					allRoles);
			add(authorizationPolicy);
			authorizationPolicy = new AuthorizationPolicy(HTTPMethod.DELETE, "/role/{id}", allRoles);
			add(authorizationPolicy);
			authorizationPolicy = new AuthorizationPolicy(HTTPMethod.GET, "/role/{id}", allRoles);
			add(authorizationPolicy);
			authorizationPolicy = new AuthorizationPolicy(HTTPMethod.PUT, "/role/{id}", allRoles);
			add(authorizationPolicy);
		}
	}

	private class AuthorizationPolicy {
		private HTTPMethod method;
		private String URL;
		private List<Role> roles;

		public AuthorizationPolicy(HTTPMethod method, String URL, List<Role> roles) {
			this.method = method;
			this.URL = URL;
			this.roles = roles;
		}

		public HTTPMethod getMethod() {
			return method;
		}

		public String getURL() {
			return URL;
		}

		public List<Role> getRoles() {
			return roles;
		}
	}
}
