package com.chiropoint.backend.service;

import com.chiropoint.backend.domain.models.Appointment;
import com.chiropoint.backend.domain.models.Patient;
import org.springframework.stereotype.Service;

@Service
public class MailingService {

    public boolean notifyAppointment(Patient patient, Appointment appointment) {
        // TODO - implement
        return true;
    }
}
