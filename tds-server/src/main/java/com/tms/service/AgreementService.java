package com.tms.service;

import com.tms.entity.Agreement;

import java.util.List;

public interface AgreementService {
    Agreement createAgreement(Agreement agreement);
    Agreement updateAgreement(Long id, Agreement agreement);
    void deleteAgreement(Long id);
    Agreement getAgreementById(Long id);
    List<Agreement> getAllAgreements();
    List<Agreement> getAgreementsByStatus(String status);
    List<Agreement> getAgreementsByCreatorId(Long creatorId);
}