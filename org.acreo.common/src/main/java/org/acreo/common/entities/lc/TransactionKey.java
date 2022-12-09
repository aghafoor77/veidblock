package org.acreo.common.entities.lc;

import java.io.Serializable;

public class TransactionKey implements Serializable {
	protected String ref;
	protected long counter;

	public TransactionKey() {
	}

	public TransactionKey(String ref, long counter) {
		this.ref = ref;
		this.counter = counter;
	}
}
