package com.bank.orchestrator_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import com.bank.orchestrator_service.dto.AccountDTO;

@Service
public class OrchestralAccountService {
	
	 @Autowired
	    private RestTemplate restTemplate;
	 
	 private final String ACCOUNT_SERVICE_BASE_URL = "http://ACCOUNTSERVICE/accounts";

	public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO accountDTO) {
	
		accountDTO.setActionType("CREATE");
        return restTemplate.postForEntity(ACCOUNT_SERVICE_BASE_URL + "/save", accountDTO, AccountDTO.class);
}
	
	public ResponseEntity<AccountDTO> updateAccount(@RequestBody AccountDTO accountDTO) {
		
		 accountDTO.setActionType("UPDATE");

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);

	        HttpEntity<AccountDTO> request = new HttpEntity<>(accountDTO, headers);

	        ResponseEntity<AccountDTO> response = restTemplate.exchange(
	                ACCOUNT_SERVICE_BASE_URL + "/save",
	                HttpMethod.POST,
	                request,
	                AccountDTO.class
	        );

	        return ResponseEntity.ok(response.getBody());
	}
	
	public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
		
		 AccountDTO dto = new AccountDTO();
	        dto.setActionType("DELETE");
	        dto.setId(id);

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);

	        HttpEntity<AccountDTO> request = new HttpEntity<>(dto, headers);

	        restTemplate.postForEntity(ACCOUNT_SERVICE_BASE_URL + "/save", request, Void.class);
	        return ResponseEntity.ok().build();
	        
	}
	
	 public ResponseEntity<AccountDTO> getAccountByNumber(@PathVariable Long id) {
		 
		 return restTemplate.getForEntity(
	                ACCOUNT_SERVICE_BASE_URL + "/" + id,
	                AccountDTO.class
	        );
	 }
	
	 public ResponseEntity<List> getAllAccounts() {
		 
		 return restTemplate.getForEntity(ACCOUNT_SERVICE_BASE_URL + "/all", List.class);
		 
	 }
	
	
	
	
	
	
	
	
	
	
	
	
	
	}

