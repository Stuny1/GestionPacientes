package com.chiropoint.backend.mocks;

import com.chiropoint.backend.domain.models.Address;
import com.chiropoint.backend.domain.models.Patient;
import com.chiropoint.backend.domain.models.Phone;
import com.chiropoint.backend.domain.repositories.PatientRepository;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class PatientMock {

    public final static ArrayList<Patient> patients = new ArrayList<>();

    static {
        patients.add(
                new Patient(
                        "111111111", "a@a.a",
                        "A", "A", "A", "A",
                        new Address("calle", 1, "i", 11, 0, "Somewhere", "RM", "Chile"),
                        new Phone("+56956373304", true, true)
                )
        );
        patients.add(
                new Patient(
                        "222222222", "b@b.b",
                        "B", "B", "B", "B",
                        new Address("calle", 2, null, null, 0, "Somewhere", "RM", "Chile"),
                        new Phone("+56956373304", true, true)
                )
        );

        addIds();
    }

    public static void setUpRepository(PatientRepository repo) {
        for (int idx = 0; idx < patients.size(); idx ++) {
            Mockito.when(repo.findById(idx)).thenReturn(Optional.of(patients.get(idx)));
        }

        Mockito.when(repo.findById(999)).thenReturn(Optional.empty());
    }

    private static void addIds() {
        for (int idx = 0; idx < patients.size(); idx ++) {
            patients.get(idx).setId(idx);
        }
    }

    public static void addAppointments() {
        patients.forEach(patient -> {
            if (patient.getId() == null) {
                return;
            }
            patient.setAppointments(AppointmentMock.appointments.stream().filter(ap -> {
                if (ap.getPatient() == null) {
                    return false;
                }

                return ap.getPatient().getId().equals(patient.getId());
            }).collect(Collectors.toList()));
        });
    }
}
