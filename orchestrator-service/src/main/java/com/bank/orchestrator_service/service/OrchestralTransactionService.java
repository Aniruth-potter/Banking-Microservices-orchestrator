package com.bank.orchestrator_service.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import com.bank.orchestrator_service.dto.TransactionDTO;

import io.github.resilience4j.retry.annotation.Retry;

@Service
public class OrchestralTransactionService {
	
	@Autowired
	 RestTemplate restTemplate;
	
	@Autowired
	private KafkaTemplate<String, TransactionDTO> kafkaTemplate;

	private final String topic = "failed-transactions";
	
	private static final Logger logger = (Logger) LoggerFactory.getLogger(OrchestralTransactionService.class);
	
	private final String ACCOUNT_SERVICE_BASE_URL = "http://ACCOUNTSERVICE/accounts";
	private final String transactionUrl = "http://TRANSACTIONSERVICE/transactions/save";
	
	public ResponseEntity<BigDecimal> depositAmount(@RequestBody TransactionDTO transactionDTO) {
		
		 // 1. Deposit to account with retry
	    depositToAccountWithRetry(transactionDTO);

	    // 2. Save transaction with its own retry
	    saveTransactionWithRetry(transactionDTO);

	    return ResponseEntity.status(HttpStatus.CREATED).body(transactionDTO.getAmount());
	}

public ResponseEntity<BigDecimal> withdrawAmount(@RequestBody TransactionDTO transactionDTO) {
	    
		
	// 1. Deposit to account with retry
	withdrawToAccountWithRetry(transactionDTO);

    // 2. Save transaction with its own retry
    saveTransactionWithRetry(transactionDTO);

	    return ResponseEntity.status(HttpStatus.CREATED).body(transactionDTO.getAmount());
	}
	
	public ResponseEntity<BigDecimal> transferAmount(@RequestBody TransactionDTO transactionDTO) {
	    
		
		// 1. Deposit to account with retry
		transferToAccountWithRetry(transactionDTO);

	    // 2. Save transaction with its own retry
	    saveTransactionWithRetry(transactionDTO);

	    return ResponseEntity.status(HttpStatus.CREATED).body(transactionDTO.getAmount());
	}
	
	@Retry(name = "depositAccountService", fallbackMethod = "accountFallback")
	public void depositToAccountWithRetry(TransactionDTO dto) {
	    String url = ACCOUNT_SERVICE_BASE_URL + dto.getSourceAccountNumber() + "/deposit";
	    Map<String, BigDecimal> body = Map.of("amount", dto.getAmount());

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

	    HttpEntity<Map<String, BigDecimal>> request = new HttpEntity<>(body, headers);
	    restTemplate.exchange(url, HttpMethod.PUT, request, Map.class);
	}
	
	@Retry(name = "withdrawAccountService", fallbackMethod = "accountFallback")
	public void withdrawToAccountWithRetry(TransactionDTO dto) {
		// Prepare request to AccountService
	    String accountServiceUrl = ACCOUNT_SERVICE_BASE_URL + dto.getSourceAccountNumber() +"/withdraw";
	    Map<String, BigDecimal> requestBody = new HashMap<>();
	    requestBody.put("amount", dto.getAmount());
	    
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<Map<String, BigDecimal>> requestEntity = new HttpEntity<>(requestBody, headers);

	    restTemplate.exchange(
	            accountServiceUrl,
	            HttpMethod.PUT,
	            requestEntity,
	            Map.class
	    );
	}
	
	@Retry(name = "transferAccountService", fallbackMethod = "accountFallback")
	public void transferToAccountWithRetry(TransactionDTO dto) {
		String accountServiceUrl = ACCOUNT_SERVICE_BASE_URL + dto.getSourceAccountNumber() +
	    		"/transfer/" + dto.getTargetAccountNumber();

	    Map<String, BigDecimal> requestBody = new HashMap<>();
	    requestBody.put("amount", dto.getAmount());

	    
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<Map<String, BigDecimal>> requestEntity = new HttpEntity<>(requestBody, headers);

	     restTemplate.exchange(
	            accountServiceUrl,
	            HttpMethod.PUT,
	            requestEntity,
	            Map.class);
	}


	@Retry(name = "transactionService", fallbackMethod = "transactionFallback")
	public void saveTransactionWithRetry(TransactionDTO dto) {
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

	    HttpEntity<TransactionDTO> request = new HttpEntity<>(dto, headers);
	    restTemplate.postForEntity(transactionUrl, request, Void.class);
	}
	

	
	public void accountFallback(TransactionDTO dto, Throwable t) {
		logger.error("Exception occurred in account service", t.getMessage(),t);
	}

	public void transactionFallback(TransactionDTO dto, Throwable t) {
		logger.error("Transaction failed, sending to Kafka. Reason: {}", t.getMessage(), t);

	    try {
	        kafkaTemplate.send("failed-transactions", dto);
	        logger.info("Sent failed transaction to Kafka topic successfully.");
	    } catch (Exception ex) {
	        logger.error("Kafka send failed. Message lost or needs manual handling. Reason: {}", ex.getMessage(), ex);
	    }
	}

}
