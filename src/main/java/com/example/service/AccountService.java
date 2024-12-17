package com.example.service;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
@Transactional
public class AccountService {
    AccountRepository accountRepository;
    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account addAccount(Account account){
        return accountRepository.save(account);
    }
    public boolean checkUser(Account account){
        return accountRepository.existsByUsername(account.getUsername());
    }
    public Account getAccount (Account account){
        return accountRepository.findByUsernameAndPassword(account.getUsername(), account.getPassword());
    }
    public Account getUserById(int userId){
        return accountRepository.findByAccountId(userId);
    }

}
