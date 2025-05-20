package com.bank.orchestrator_service.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.bank.orchestrator_service.dto.TransactionDTO;
import com.bank.orchestrator_service.service.OrchestralTransactionService;


@RestController
@RequestMapping("/orchestral")
public class orchestralTransactionController {
	
	@Autowired
	OrchestralTransactionService orchestralTransactionService;

	@PostMapping("/transaction/deposit")
	public ResponseEntity<BigDecimal> depositAmount(@RequestBody TransactionDTO transactionDTO) {
	    
	    return orchestralTransactionService.depositAmount(transactionDTO);
	    
	}
	
	@PostMapping("/transaction/withdraw")
	public ResponseEntity<BigDecimal> withdrawAmount(@RequestBody TransactionDTO transactionDTO) {

	    return  orchestralTransactionService.withdrawAmount(transactionDTO);
	}
	
	@PostMapping("/transaction/transfer")
	public ResponseEntity<BigDecimal> transferAmount(@RequestBody TransactionDTO transactionDTO) {
	    

	    return  orchestralTransactionService.transferAmount(transactionDTO);
	}
	
	
}
