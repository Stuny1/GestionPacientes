package com.chiropoint.backend.mocks;

import com.chiropoint.backend.domain.models.Address;
import com.chiropoint.backend.domain.models.Office;
import com.chiropoint.backend.domain.models.Phone;
import com.chiropoint.backend.domain.repositories.OfficeRepository;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Optional;

public class OfficeMock {

    public final static ArrayList<Office> offices = new ArrayList<>();

    static {
        offices.add(
                new Office(
                        OrganizationMock.VIDACHIROPRACTIC, "Centro",
                        new Address("Moneda", 2000, null, 76, 0, "Santiago", "RM", "Chile"),
                        new Phone("+56950005000", false, true)
                )
        );

        offices.add(
                new Office(
                        OrganizationMock.VIDACHIROPRACTIC, "Valpo",
                        new Address("??", 2000, "A", 76, 0, "Valparaiso", "RM", "Chile"),
                        new Phone("+56950005000", true, true)
                )
        );

        addIds();
    }

    public static void setUpRepository(OfficeRepository repo) {
        for (int idx = 0; idx < offices.size(); idx ++) {
            Mockito.when(repo.findById(idx)).thenReturn(Optional.of(offices.get(idx)));
        }

        Mockito.when(repo.findById(999)).thenReturn(Optional.empty());
    }

    private static void addIds() {
        for (int idx = 0; idx < offices.size(); idx ++) {
            offices.get(idx).setId(idx);
        }
    }

}
