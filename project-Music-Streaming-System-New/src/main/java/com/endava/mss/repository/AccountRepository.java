package com.endava.mss.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.endava.mss.entities.Account;

public interface AccountRepository extends JpaRepository<Account,Long> {

}
