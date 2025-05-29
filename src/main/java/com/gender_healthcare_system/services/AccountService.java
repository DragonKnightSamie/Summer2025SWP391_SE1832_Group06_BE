package com.gender_healthcare_system.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gender_healthcare_system.entities.enu.AccountStatus;
import com.gender_healthcare_system.entities.user.Account;
import com.gender_healthcare_system.entities.user.AccountInfoDetails;
import com.gender_healthcare_system.entities.user.Customer;
import com.gender_healthcare_system.entities.user.Role;
import com.gender_healthcare_system.iservices.IAccountService;
import com.gender_healthcare_system.payloads.CustomerPayload;
import com.gender_healthcare_system.repositories.AccountRepo;
import com.gender_healthcare_system.repositories.CustomerRepo;
import com.gender_healthcare_system.repositories.RoleRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService implements IAccountService {

    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> accountInfo = accountRepo.findAccountsByUsername(username);

        return accountInfo
                .map(AccountInfoDetails::new)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + username));
    }

    @Transactional
    public void createCustomerAccount(CustomerPayload payload) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        Account account = new Account();
        Customer customer = new Customer();

        account.setUsername(payload.getUsername());
        account.setPassword(payload.getPassword());
        account.setStatus(AccountStatus.ACTIVE);

        Role role = roleRepo.findByRoleId(2);
        account.setRole(role);

        //account.setRoleId(2);

        accountRepo.saveAndFlush(account);

        //customer.setCustomerId(account.getAccountId());
        customer.setAccount(account);

        customer.setFullName(payload.getFullName());
        customer.setGender(payload.getGender());

        String GenderSpecificDetails = mapper.writeValueAsString(
                payload.getGenderSpecificDetails());
        customer.setGenderSpecificDetails(GenderSpecificDetails);

        customer.setDateOfBirth(payload.getDateOfBirth());
        customer.setPhone(payload.getPhone());
        customer.setEmail(payload.getEmail());
        customer.setAddress(payload.getAddress());
        
        customerRepo.saveAndFlush(customer);
    }




}
