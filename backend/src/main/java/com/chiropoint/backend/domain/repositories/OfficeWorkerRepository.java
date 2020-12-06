package com.chiropoint.backend.domain.repositories;

import com.chiropoint.backend.domain.models.OfficeWorker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OfficeWorkerRepository extends JpaRepository<OfficeWorker, Integer> {

    String OFFICE_PERSON_JOINED = "SELECT worker.*, person.* FROM office_workers AS r " +
            "INNER JOIN person ON r.worker_id = person.id " +
            "INNER JOIN office_worker AS worker ON r.worker_id = worker.id " +
            "INNER JOIN office ON r.office_id = office.id";

    @Query(value = OFFICE_PERSON_JOINED + " WHERE MOD(worker.role, 2) = 1 AND office.id = ?", nativeQuery = true)
    List<OfficeWorker> findAllChiropractorsByOffice(Integer officeId);

    @Query(value = OFFICE_PERSON_JOINED + " WHERE MOD(worker.role, 2) = 0  AND office.id = ?", nativeQuery = true)
    List<OfficeWorker> findAllAssistantsByOffice(Integer officeId);

}
