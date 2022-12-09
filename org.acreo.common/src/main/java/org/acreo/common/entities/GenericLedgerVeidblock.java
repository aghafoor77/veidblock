package org.acreo.common.entities;

import java.util.List;

public class GenericLedgerVeidblock {

	private GenericVeidblockHeader genericVeidblockHeader;
	private List<GenericVeidblock> genericVeidblockList;
	public GenericVeidblockHeader getGenericVeidblockHeader() {
		return genericVeidblockHeader;
	}
	public void setGenericVeidblockHeader(GenericVeidblockHeader genericVeidblockHeader) {
		this.genericVeidblockHeader = genericVeidblockHeader;
	}
	public List<GenericVeidblock> getGenericVeidblockList() {
		return genericVeidblockList;
	}
	public void setGenericVeidblockList(List<GenericVeidblock> genericVeidblockList) {
		this.genericVeidblockList = genericVeidblockList;
	}	
}
