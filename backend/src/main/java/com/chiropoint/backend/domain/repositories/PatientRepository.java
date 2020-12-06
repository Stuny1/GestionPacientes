package com.chiropoint.backend.domain.repositories;

import com.chiropoint.backend.domain.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {

    String PERSON_JOINED = "SELECT patient.*, person.* FROM patient INNER JOIN person ON patient.id = person.id";

    Patient findByIdDocument(String idDocument);

    @Query(value = PERSON_JOINED +
            " WHERE patient.id IN (SELECT patient_id FROM appointment WHERE office_id = ? AND archived = 0)",
            nativeQuery = true)
    List<Patient> findByOffice(int officeId);

}


