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


@RestController
@RequestMapping("/orchestral")
public class orchestralTransactionController {
	
	@Autowired
	 RestTemplate restTemplate;

	@PostMapping("/transaction/deposit")
	public ResponseEntity<BigDecimal> depositAmount(@RequestBody TransactionDTO transactionDTO) {
	    
	    // Step 1: Call AccountService to add amount
	    String accountUrl = "http://localhost:5050/accounts/" + transactionDTO.getSourceAccountNumber() + "/deposit";
	    
	    Map<String, BigDecimal> amountBody = new HashMap<>();
	    amountBody.put("amount", transactionDTO.getAmount());

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<Map<String, BigDecimal>> request = new HttpEntity<>(amountBody, headers);

	    restTemplate.exchange(accountUrl, HttpMethod.PUT, request, Map.class);

	    // Step 2: Call TransactionService to save transaction
	    String transactionUrl = "http://localhost:5051/transactions/save";

	    HttpEntity<TransactionDTO> txRequest = new HttpEntity<>(transactionDTO, headers);
	    restTemplate.postForEntity(transactionUrl, txRequest, Void.class);  // Or expect confirmation DTO

	    return ResponseEntity.status(HttpStatus.CREATED).body(transactionDTO.getAmount());
	}

	
	@PostMapping("/transaction/withdraw")
	public ResponseEntity<BigDecimal> withdrawAmount(@RequestBody TransactionDTO transactionDTO) {
	    
		
		// Prepare request to AccountService
	    String accountServiceUrl = "http://localhost:5050/accounts/" + transactionDTO.getSourceAccountNumber() +"/withdraw";
	    Map<String, BigDecimal> requestBody = new HashMap<>();
	    requestBody.put("amount", transactionDTO.getAmount());
	    
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<Map<String, BigDecimal>> requestEntity = new HttpEntity<>(requestBody, headers);

	    restTemplate.exchange(
	            accountServiceUrl,
	            HttpMethod.PUT,
	            requestEntity,
	            Map.class
	    );

	    // Step 2: Call TransactionService to save transaction
	    String transactionUrl = "http://localhost:5051/transactions/save";

	    HttpEntity<TransactionDTO> txRequest = new HttpEntity<>(transactionDTO, headers);
	    restTemplate.postForEntity(transactionUrl, txRequest, Void.class);  // Or expect confirmation DTO

	    return ResponseEntity.status(HttpStatus.CREATED).body(transactionDTO.getAmount());
	}
	
	@PostMapping("/transaction/transfer")
	public ResponseEntity<BigDecimal> transferAmount(@RequestBody TransactionDTO transactionDTO) {
	    
		
		// Prepare request to AccountService
	    String accountServiceUrl = "http://localhost:5050/accounts/" + transactionDTO.getSourceAccountNumber() +
	    		"/transfer/" + transactionDTO.getTargetAccountNumber();

	    Map<String, BigDecimal> requestBody = new HashMap<>();
	    requestBody.put("amount", transactionDTO.getAmount());

	    
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<Map<String, BigDecimal>> requestEntity = new HttpEntity<>(requestBody, headers);

	     restTemplate.exchange(
	            accountServiceUrl,
	            HttpMethod.PUT,
	            requestEntity,
	            Map.class
	    );

	    // Step 2: Call TransactionService to save transaction
	    String transactionUrl = "http://localhost:5051/transactions/save";

	    HttpEntity<TransactionDTO> txRequest = new HttpEntity<>(transactionDTO, headers);
	    restTemplate.postForEntity(transactionUrl, txRequest, Void.class);  // Or expect confirmation DTO

	    return ResponseEntity.status(HttpStatus.CREATED).body(transactionDTO.getAmount());
	}
	
	
}
