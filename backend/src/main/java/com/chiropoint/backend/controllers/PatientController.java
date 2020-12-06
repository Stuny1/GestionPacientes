package com.chiropoint.backend.controllers;

import com.chiropoint.backend.controllers.handlers.ErrorResponse;
import com.chiropoint.backend.domain.models.Patient;
import com.chiropoint.backend.domain.repositories.PatientRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    @GetMapping("/")
    public ResponseEntity getAllPatients() {
        List<Patient> patients = patientRepository.findAll();

        if (patients == null || patients.isEmpty()) {
            return ErrorResponse.generic404().asResponseEntity();
        }

        return ResponseEntity.ok(patients);
    }

    @PostMapping("/")
    public ResponseEntity queryPatients(@RequestBody PatientQueryRequest query) {
        List<Patient> patients = patientRepository.findByOffice(query.getOfficeId());

        if (patients == null || patients.isEmpty()) {
            return ErrorResponse.generic404().asResponseEntity();
        }

        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}")
    public ResponseEntity getPatient(@PathVariable("id") Integer id) {
        Patient patient = id == null ? null : patientRepository.findById(id).orElse(null);
        if (patient == null) {
            return ErrorResponse.generic404().asResponseEntity();
        }

        return ResponseEntity.ok(patient);
    }

    @Data
    public static class PatientQueryRequest {
        private Integer officeId;
    }

}
