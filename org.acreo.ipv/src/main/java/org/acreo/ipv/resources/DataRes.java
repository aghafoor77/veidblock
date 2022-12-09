package org.acreo.ipv.resources;

import java.util.List;

public class DataRes {
	private List data;

	public DataRes(){
		
	}
	
	public DataRes(List data) {
		super();
		this.data = data;
	}

	public List getData() {
		return data;
	}

	public void setData(List data) {
		this.data = data;
	}


	
}

