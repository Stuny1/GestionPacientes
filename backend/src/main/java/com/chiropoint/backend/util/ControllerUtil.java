package com.chiropoint.backend.util;

import com.chiropoint.backend.domain.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class ControllerUtil {

    public static void checkUserOfficeAccess(User user, Integer officeId) throws HttpClientErrorException {
        if (user.canAccessOffice(officeId)) {
            return;
        }

        throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "Cannot access that office's data");
    }
}
