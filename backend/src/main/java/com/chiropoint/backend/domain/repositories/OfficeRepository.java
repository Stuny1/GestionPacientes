package com.chiropoint.backend.domain.repositories;

import com.chiropoint.backend.domain.models.Office;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfficeRepository extends JpaRepository<Office, Integer> {

}
