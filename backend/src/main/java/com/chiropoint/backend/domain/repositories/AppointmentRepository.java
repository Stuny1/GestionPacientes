package com.chiropoint.backend.domain.repositories;

import com.chiropoint.backend.domain.models.Appointment;
import com.chiropoint.backend.domain.models.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    List<Appointment> findAllByOffice(Office office);

}
