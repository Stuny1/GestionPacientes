package com.chiropoint.backend.mocks;

import com.chiropoint.backend.domain.models.Appointment;
import com.chiropoint.backend.domain.repositories.AppointmentRepository;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public class AppointmentMock {
    public final static ArrayList<Appointment> appointments = new ArrayList<>();

    static {
        appointments.add(new Appointment(
                AppointmentTypeMock.NORMAL, new Date(),
                OfficeMock.offices.get(0), PatientMock.patients.get(0), OfficeWorkerMock.workers.get(0)
                ));

        appointments.add(new Appointment(
                AppointmentTypeMock.EVAL, new Date(),
                OfficeMock.offices.get(0), PatientMock.patients.get(0), OfficeWorkerMock.workers.get(0)
        ));

        addIds();
    }

    public static void setUpRepository(AppointmentRepository repo) {
        for (int idx = 0; idx < appointments.size(); idx ++) {
            Mockito.when(repo.findById(idx)).thenReturn(Optional.of(appointments.get(idx)));
        }

        Mockito.when(repo.findById(999)).thenReturn(Optional.empty());
    }

    private static void addIds() {
        for (int idx = 0; idx < appointments.size(); idx ++) {
            appointments.get(idx).setId(idx);
        }
    }
}
