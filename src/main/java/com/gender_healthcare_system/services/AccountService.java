package com.gender_healthcare_system.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gender_healthcare_system.entities.enu.AccountStatus;
import com.gender_healthcare_system.entities.enu.Gender;
import com.gender_healthcare_system.entities.todo.Certificate;
import com.gender_healthcare_system.entities.user.*;
import com.gender_healthcare_system.exceptions.AppException;
import com.gender_healthcare_system.iservices.IAccountService;
import com.gender_healthcare_system.payloads.todo.CertificateRegisterPayload;
import com.gender_healthcare_system.payloads.user.ConsultantRegisterPayload;
import com.gender_healthcare_system.payloads.user.CustomerPayload;
import com.gender_healthcare_system.payloads.user.ManagerRegisterPayload;
import com.gender_healthcare_system.payloads.user.StaffRegisterPayload;
import com.gender_healthcare_system.repositories.*;
import com.gender_healthcare_system.utils.UtilFunctions;
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

    private final ConsultantRepo consultantRepo;

    private final CertificateRepo certificateRepo;

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

        accountRepo.saveAndFlush(account);

        customer.setAccount(account);

        customer.setFullName(payload.getFullName());
        customer.setGender(payload.getGender());

        UtilFunctions.validatePeriodDetails
                (payload.getGender(), payload.getGenderSpecificDetails());

        String GenderSpecificDetails = null;
        if(payload.getGender() == Gender.FEMALE) {

            GenderSpecificDetails = mapper.writeValueAsString(
                    payload.getGenderSpecificDetails());
        }
        customer.setGenderSpecificDetails(GenderSpecificDetails);

        customer.setDateOfBirth(payload.getDateOfBirth());
        customer.setPhone(payload.getPhone());
        customer.setEmail(payload.getEmail());
        customer.setAddress(payload.getAddress());
        
        customerRepo.saveAndFlush(customer);
    }

    @Transactional
    public void updateCustomerStatus(int customerId, AccountStatus status){
        boolean customerExist = customerRepo.existsById(customerId);

        if(!customerExist) {

            throw new AppException(404, "Customer not found");
        }

        boolean accountStatusIdentical = accountRepo
                .existsAccountByAccountIdAndStatus(customerId, status);

        if(!accountStatusIdentical){

            accountRepo.updateAccountStatus(customerId, status);
        }
    }

    @Transactional
    public void deleteCustomerById(int customerId) {
        boolean customerExist = customerRepo.existsById(customerId);

        if(!customerExist) {

            throw new AppException(404, "Customer not found");
        }

        customerRepo.deleteCustomerById(customerId);
        accountRepo.deleteAccountById(customerId);
    }


    public void createConsultantAccount(ConsultantRegisterPayload payload){
        Account account = new Account();
        Consultant consultant = new Consultant();
        Certificate certificate;

        account.setUsername(payload.getUsername());
        account.setPassword(payload.getPassword());
        account.setStatus(AccountStatus.ACTIVE);

        Role role = roleRepo.findByRoleId(4);
        account.setRole(role);

        accountRepo.saveAndFlush(account);

        consultant.setAccount(account);

        consultant.setFullName(payload.getFullName());
        consultant.setAvatarUrl(payload.getAvatarUrl());
        consultant.setPhone(payload.getPhone());
        consultant.setEmail(payload.getEmail());
        consultant.setAddress(payload.getAddress());

        consultantRepo.save(consultant);

        for(CertificateRegisterPayload item: payload.getCertificates()) {

            UtilFunctions.validateIssueDateAndExpiryDate
                    (item.getIssueDate(), item.getExpiryDate());

            certificate = new Certificate();
            certificate.setConsultant(consultant);

            certificate.setCertificateName(item.getCertificateName());
            certificate.setIssuedBy(item.getIssuedBy());
            certificate.setIssueDate(item.getIssueDate());
            certificate.setImageUrl(item.getImageUrl());
            certificate.setExpiryDate(item.getExpiryDate());
            certificate.setDescription(item.getDescription());

            certificateRepo.saveAndFlush(certificate);
        }
    }

    @Transactional
    public void deleteConsultantById(int consultantId) {
        boolean consultantExist = consultantRepo.existsById(consultantId);

        if(!consultantExist) {

            throw new AppException(404, "Consultant not found");
        }

        consultantRepo.deleteConsultantById(consultantId);
        accountRepo.deleteAccountById(consultantId);
    }

    //createManagerAccount by Admin
    @Transactional
    public void createManagerAccount(ManagerRegisterPayload payload) {
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

    @Transactional
    public void deleteManagerById(int managerId) {
        boolean managerExist = managerRepo.existsById(managerId);

        if(!managerExist) {

            throw new AppException(404, "Manager not found");
        }

        managerRepo.deleteManagerById(managerId);
        accountRepo.deleteAccountById(managerId);
    }

    //createStaffAccount by Manager
    @Transactional
    public void createStaffAccount(StaffRegisterPayload payload) {
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

    @Transactional
    public void deleteStaffById(int staffId) {
        boolean staffExist = staffRepo.existsById(staffId);

        if(!staffExist) {

            throw new AppException(404, "Staff not found");
        }

        staffRepo.deleteStaffById(staffId);
        accountRepo.deleteAccountById(staffId);
    }








}
