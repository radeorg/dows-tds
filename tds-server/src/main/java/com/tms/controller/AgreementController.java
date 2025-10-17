package com.tms.controller;

import com.tms.entity.Agreement;
import com.tms.service.AgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agreements")
public class AgreementController {

    @Autowired
    private AgreementService agreementService;

    @PostMapping
    public ResponseEntity<Agreement> createAgreement(@RequestBody Agreement agreement) {
        return ResponseEntity.ok(agreementService.createAgreement(agreement));
    }

    @GetMapping
    public ResponseEntity<List<Agreement>> getAllAgreements() {
        return ResponseEntity.ok(agreementService.getAllAgreements());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Agreement>> getAgreementsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(agreementService.getAgreementsByStatus(status));
    }

    @GetMapping("/creator/{creatorId}")
    public ResponseEntity<List<Agreement>> getAgreementsByCreator(@PathVariable Long creatorId) {
        return ResponseEntity.ok(agreementService.getAgreementsByCreatorId(creatorId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agreement> getAgreementById(@PathVariable Long id) {
        return ResponseEntity.ok(agreementService.getAgreementById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Agreement> updateAgreement(@PathVariable Long id, @RequestBody Agreement agreement) {
        return ResponseEntity.ok(agreementService.updateAgreement(id, agreement));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgreement(@PathVariable Long id) {
        agreementService.deleteAgreement(id);
        return ResponseEntity.noContent().build();
    }
}