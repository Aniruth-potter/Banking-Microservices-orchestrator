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
import com.bank.orchestrator_service.service.OrchestralAccountService;

@RestController
@RequestMapping("/orchestral/accounts")
public class OrchestralAccountController {
    
    @Autowired
    private OrchestralAccountService orchestralAccountService;

    // ✅ 1. Create account
    @PostMapping("/create")
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO accountDTO) {
        accountDTO.setActionType("CREATE");
        return orchestralAccountService.createAccount(accountDTO);
    }

    // ✅ 2. Update account
    @PutMapping("/update")
    public ResponseEntity<AccountDTO> updateAccount(@RequestBody AccountDTO accountDTO) {
        
        return orchestralAccountService.updateAccount(accountDTO);
        
    }

    // ✅ 3. Delete account
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
   
        return orchestralAccountService.deleteAccount(id);
        
    }

    // ✅ 4. Get account by ID
    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountByNumber(@PathVariable Long id) {
        return orchestralAccountService.getAccountByNumber(id);
    }
    

    // ✅ 5. Get all accounts
    @GetMapping("/all")
    public ResponseEntity<List> getAllAccounts() {
        return orchestralAccountService.getAllAccounts();
    }
}



