package com.bank.orchestrator_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.bank.orchestrator_service.dto.AccountDTO;

@RestController
@RequestMapping("/orchestral/accounts")
public class OrchestralAccountController {

    @Autowired
    private RestTemplate restTemplate;

    private final String ACCOUNT_SERVICE_BASE_URL = "http://localhost:5050/accounts";

    // ✅ 1. Create account
    @PostMapping("/create")
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO accountDTO) {
        accountDTO.setActionType("CREATE");
        return restTemplate.postForEntity(ACCOUNT_SERVICE_BASE_URL + "/save", accountDTO, AccountDTO.class);
    }

    // ✅ 2. Update account
    @PutMapping("/update")
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

    // ✅ 3. Delete account
    @DeleteMapping("/delete/{id}")
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

    // ✅ 4. Get account by ID
    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountByNumber(@PathVariable Long id) {
        return restTemplate.getForEntity(
                ACCOUNT_SERVICE_BASE_URL + "/" + id,
                AccountDTO.class
        );
    }

    // ✅ 5. Get all accounts
    @GetMapping("/all")
    public ResponseEntity<List> getAllAccounts() {
        return restTemplate.getForEntity(ACCOUNT_SERVICE_BASE_URL + "/all", List.class);
    }
}



