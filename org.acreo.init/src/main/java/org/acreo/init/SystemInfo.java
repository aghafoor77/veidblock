package org.acreo.init;

import java.util.Date;

public class SystemInfo {

	private String module;
	public String version;
	public String orgnization;
	public String logoPath;
	public Date updated;

	public SystemInfo(String module, String version, String orgnization, String logoPath, Date updated) {
		this.module = module;
		this.version = version;
		this.orgnization = orgnization;
		this.logoPath = logoPath;
		this.updated = updated;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getOrgnization() {
		return orgnization;
	}

	public void setOrgnization(String orgnization) {
		this.orgnization = orgnization;
	}

	public String getLogoPath() {
		return logoPath;
	}

	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

}
