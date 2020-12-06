package com.chiropoint.backend.domain.repositories;

import com.chiropoint.backend.domain.models.ChronicCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChronicConditionRepository extends JpaRepository<ChronicCondition, Integer> {

}
