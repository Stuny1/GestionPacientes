package com.chiropoint.backend.mocks;

import com.chiropoint.backend.domain.models.AppointmentType;
import com.chiropoint.backend.domain.repositories.AppointmentTypeRepository;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Optional;

public class AppointmentTypeMock {

    public final static ArrayList<AppointmentType> types = new ArrayList<>();

    public final static AppointmentType NORMAL = new AppointmentType("Normal");
    public final static AppointmentType EVAL = new AppointmentType("Evaluation");

    static {
        types.add(NORMAL);
        types.add(EVAL);

        addIds();
    }

    public static void setUpRepository(AppointmentTypeRepository repo) {
        for (int idx = 0; idx < types.size(); idx ++) {
            Mockito.when(repo.findById(idx)).thenReturn(Optional.of(types.get(idx)));
        }

        Mockito.when(repo.findById(999)).thenReturn(Optional.empty());
    }

    private static void addIds() {
        for (int idx = 0; idx < types.size(); idx ++) {
            types.get(idx).setId(idx);
        }
    }

}
