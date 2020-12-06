package com.chiropoint.backend.mocks;

import com.chiropoint.backend.domain.models.Organization;

import java.util.ArrayList;

public class OrganizationMock {

    public final static ArrayList<Organization> organizations = new ArrayList<>();

    public final static Organization VIDACHIROPRACTIC = new Organization("VidaChiropractic");

    static {
        organizations.add(VIDACHIROPRACTIC);

        addIds();
    }

    private static void addIds() {
        for (int idx = 0; idx < organizations.size(); idx ++) {
            organizations.get(idx).setId(idx);
        }
    }
}
