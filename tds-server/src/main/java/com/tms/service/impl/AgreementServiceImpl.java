package com.tms.service.impl;

import com.tms.entity.Agreement;
import com.tms.repository.AgreementRepository;
import com.tms.service.AgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgreementServiceImpl implements AgreementService {

    @Autowired
    private AgreementRepository agreementRepository;

    @Override
    public Agreement createAgreement(Agreement agreement) {
        return agreementRepository.save(agreement);
    }

    @Override
    public Agreement updateAgreement(Long id, Agreement agreement) {
        Agreement existingAgreement = agreementRepository.findById(id).orElseThrow(() -> new RuntimeException("协议不存在"));
        existingAgreement.setTitle(agreement.getTitle());
        existingAgreement.setContent(agreement.getContent());
        existingAgreement.setVersion(agreement.getVersion());
        existingAgreement.setStatus(agreement.getStatus());
        existingAgreement.setUpdatedAt(LocalDateTime.now());
        return agreementRepository.save(existingAgreement);
    }

    @Override
    public void deleteAgreement(Long id) {
        agreementRepository.deleteById(id);
    }

    @Override
    public Agreement getAgreementById(Long id) {
        return agreementRepository.findById(id).orElseThrow(() -> new RuntimeException("协议不存在"));
    }

    @Override
    public List<Agreement> getAllAgreements() {
        return agreementRepository.findAll();
    }

    @Override
    public List<Agreement> getAgreementsByStatus(String status) {
        return agreementRepository.findByStatus(status);
    }

    @Override
    public List<Agreement> getAgreementsByCreatorId(Long creatorId) {
        return agreementRepository.findByCreatorId(creatorId);
    }
}