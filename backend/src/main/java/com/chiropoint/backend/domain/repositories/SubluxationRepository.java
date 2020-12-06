package com.chiropoint.backend.domain.repositories;

import com.chiropoint.backend.domain.models.Subluxation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubluxationRepository extends JpaRepository<Subluxation, Integer> {

}
