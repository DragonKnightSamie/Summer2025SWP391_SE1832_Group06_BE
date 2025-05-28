package com.gender_healthcare_system.services;

import com.gender_healthcare_system.entities.user.Account;
import com.gender_healthcare_system.entities.user.AccountInfoDetails;
import com.gender_healthcare_system.iservices.IAccountService;
import com.gender_healthcare_system.repositories.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService implements IAccountService {

    @Autowired
    private AccountRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> accountInfo = userRepo.findAccountsByUsername(username);

        return accountInfo
                .map(AccountInfoDetails::new)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + username));
    }

}
