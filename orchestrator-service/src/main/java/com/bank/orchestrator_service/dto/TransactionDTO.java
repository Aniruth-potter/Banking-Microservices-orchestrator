package com.bank.orchestrator_service.dto;

import java.math.BigDecimal;

public class TransactionDTO {

    private TransactionType type;  // DEPOSIT, WITHDRAW, TRANSFER
    private String sourceAccountNumber;  // The account performing the action
    private String targetAccountNumber;  // Used only in transfers
    private BigDecimal amount;
	public TransactionType getType() {
		return type;
	}
	public void setType(TransactionType type) {
		this.type = type;
	}
	public String getSourceAccountNumber() {
		return sourceAccountNumber;
	}
	public void setSourceAccountNumber(String sourceAccountNumber) {
		this.sourceAccountNumber = sourceAccountNumber;
	}
	public String getTargetAccountNumber() {
		return targetAccountNumber;
	}
	public void setTargetAccountNumber(String targetAccountNumber) {
		this.targetAccountNumber = targetAccountNumber;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
    
    
}
