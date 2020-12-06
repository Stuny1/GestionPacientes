package com.chiropoint.backend.mocks;

import com.chiropoint.backend.domain.models.OfficeWorker;
import com.chiropoint.backend.domain.repositories.OfficeWorkerRepository;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class OfficeWorkerMock {

    public final static ArrayList<OfficeWorker> workers = new ArrayList<>();

    private final static ArrayList<OfficeWorker.WorkerRole> chiroRoles = new ArrayList<>();

    static {
        chiroRoles.add(OfficeWorker.WorkerRole.CHIROPRACTIC);
        workers.add(
                new OfficeWorker("121212121", "wa@wa.wa", "WA", "", "WA", "", "Quiropractico", chiroRoles)
        );

        workers.add(
                new OfficeWorker("222222222", "wb@wb.wb", "WB", "", "WB", "", "Quiropractico", chiroRoles)
        );

        addIds();
    }

    public static void setUpRepository(OfficeWorkerRepository repo) {
        for (int idx = 0; idx < workers.size(); idx ++) {
            Mockito.when(repo.findById(idx)).thenReturn(Optional.of(workers.get(idx)));
        }

        Mockito.when(repo.findById(999)).thenReturn(Optional.empty());
    }

    public static void addOffices() {
        workers.get(0).getOffices().add(OfficeMock.offices.get(0));
        workers.get(1).getOffices().add(OfficeMock.offices.get(1));
    }

    private static void addIds() {
        for (int idx = 0; idx < workers.size(); idx ++) {
            workers.get(idx).setId(idx);
        }
    }

}
