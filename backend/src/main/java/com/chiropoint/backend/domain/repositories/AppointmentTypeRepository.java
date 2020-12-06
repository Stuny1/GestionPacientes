package com.chiropoint.backend.domain.repositories;

import com.chiropoint.backend.domain.models.AppointmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentTypeRepository extends JpaRepository<AppointmentType, Integer> {

}
