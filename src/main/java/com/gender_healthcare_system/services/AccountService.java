package com.gender_healthcare_system.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gender_healthcare_system.entities.enu.AccountStatus;
import com.gender_healthcare_system.entities.user.*;
import com.gender_healthcare_system.iservices.IAccountService;
import com.gender_healthcare_system.payloads.CustomerPayload;
import com.gender_healthcare_system.payloads.ManagerPayload;
import com.gender_healthcare_system.payloads.StaffPayload;
import com.gender_healthcare_system.repositories.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountService implements IAccountService {

    private final AccountRepo accountRepo;

    private final CustomerRepo customerRepo;

    private final RoleRepo roleRepo;

    private final StaffRepo staffRepo;
    private final ManagerRepo managerRepo;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<Account> accountInfo = accountRepo.findActiveAccountByUsername(username);

        return accountInfo
                .map(AccountInfoDetails::new)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Invalid Username or Password"));
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

        Role role = roleRepo.findByRoleId(5);
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

    //createManagerAccount by Admin
    public void createManagerAccount(ManagerPayload payload) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        Account account = new Account();
        Manager manager = new Manager();

        account.setUsername(payload.getUsername());
        account.setPassword(payload.getPassword());
        account.setStatus(AccountStatus.ACTIVE);
        Role role = roleRepo.findByRoleId(2); // Assuming 2 is the role ID for manager
        account.setRole(role);

        accountRepo.saveAndFlush(account);

        manager.setAccount(account);
        manager.setFullName(payload.getFullName());
        manager.setPhone(payload.getPhone());
        manager.setEmail(payload.getEmail());
        manager.setAddress(payload.getAddress());

        // Save the manager entity
        managerRepo.saveAndFlush(manager);
    }

    //createStaffAccount by Manager
    public void createStaffAccount(StaffPayload payload) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        Account account = new Account();
        Staff staff = new Staff();

        account.setUsername(payload.getUsername());
        account.setPassword(payload.getPassword());
        account.setStatus(AccountStatus.ACTIVE);
        Role role = roleRepo.findByRoleId(3); // Assuming 3 is the role ID for staff
        account.setRole(role);

        accountRepo.saveAndFlush(account);

        staff.setAccount(account);
        staff.setFullName(payload.getFullName());
        staff.setPhone(payload.getPhone());
        staff.setEmail(payload.getEmail());
        staff.setAddress(payload.getAddress());

        staffRepo.saveAndFlush(staff);
    }






}
