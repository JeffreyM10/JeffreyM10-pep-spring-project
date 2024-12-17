package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>{

    //checks if the user exists by the username
    public boolean existsByUsername(String username);

    //get the account by username and password
    public Account findByUsernameAndPassword(String username, String password);

    //get the account by accountID
    public Account findByAccountId(int accountId);
    

}
