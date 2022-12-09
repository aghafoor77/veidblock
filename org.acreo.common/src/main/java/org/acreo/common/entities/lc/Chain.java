package org.acreo.common.entities.lc;

public class Chain {
	
	private TransactionHeaderCO transactionHeader;
	private Transactions transactions;
	public TransactionHeaderCO getTransactionHeader() {
		return transactionHeader;
	}
	public void setTransactionHeader(TransactionHeaderCO transactionHeader) {
		this.transactionHeader = transactionHeader;
	}
	public Transactions getTransactions() {
		return transactions;
	}
	public void setTransactions(Transactions transactions) {
		this.transactions = transactions;
	}
}
