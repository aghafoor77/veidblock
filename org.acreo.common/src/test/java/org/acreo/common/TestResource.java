package org.acreo.common;

import org.junit.Assert;
import org.junit.Test;

public class TestResource {
	@Test
	public void fetchListOfRegResources() {
		Representation ss = new Representation(0, "");
		Assert.assertEquals(ss.getCode(),0 );

	}
}
