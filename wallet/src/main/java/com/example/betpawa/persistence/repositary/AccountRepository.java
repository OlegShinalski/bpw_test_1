package com.example.betpawa.persistence.repositary;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.betpawa.persistence.entity.Account;

@Repository
@Transactional
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Async
    CompletableFuture<List<Account>> findAllBy();

    Account findByEmail(String email);

    boolean existsByEmail(String email);

    Account getFirstById(Long id);

}