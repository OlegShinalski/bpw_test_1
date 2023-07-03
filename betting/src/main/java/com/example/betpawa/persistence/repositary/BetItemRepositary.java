package com.example.betpawa.persistence.repositary;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.betpawa.persistence.entity.BetItem;

@Repository
@Transactional
public interface BetItemRepositary extends JpaRepository<BetItem, Long> {

    Set<BetItem> findAllByIdIn(List<Long> ids);

    List<BetItem> findByBetItemId(Long betItemId);

}