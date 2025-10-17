package com.tms.repository;

import com.tms.entity.Agreement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgreementRepository extends JpaRepository<Agreement, Long> {
    List<Agreement> findByStatus(String status);
    List<Agreement> findByCreatorId(Long creatorId);
}