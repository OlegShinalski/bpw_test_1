package com.example.betpawa.persistence.repositary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.betpawa.persistence.entity.Bet;

@Repository
@Transactional
public interface BetRepositary extends JpaRepository<Bet, Long> {

    Bet getFirstById(Long id);

    List<Bet> findByAccountId(Long accountId);

}