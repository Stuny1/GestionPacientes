package com.chiropoint.backend.controllers;

import com.chiropoint.backend.controllers.handlers.ErrorResponse;
import com.chiropoint.backend.domain.models.*;
import com.chiropoint.backend.domain.repositories.PatientRepository;
import com.chiropoint.backend.dto.request.appointment.*;
import com.chiropoint.backend.dto.response.LogicResponse;
import com.chiropoint.backend.logic.AppointmentLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@RequestMapping("/appointments")
//@SessionAttributes({"user"})
public class AppointmentController {

    @Autowired
    private PatientRepository patientRepo;
    @Autowired
    private AppointmentLogic logic;

    @PostMapping("/")
    public ResponseEntity getAppointments(
            //@ModelAttribute("user") User user,
            @RequestBody AppointmentQueryRequest request
    ) {
        /*if (!canAccessOffice(user, request.getOfficeId())) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Cannot access that office's data");
        }*/

        LogicResponse<List<Appointment>> response = logic.getAppointments(request);
        if (response.getBody() == null) {
            return ErrorResponse.of(response).asResponseEntity();
        }

        return ResponseEntity.ok(response.getBody());
    }

    @PostMapping("/request")
    public ResponseEntity requestAppointment(
            //@ModelAttribute("user") User user,
            @RequestBody RequestAppointmentRequest request
    ) {
        /*if (!(user.getPerson() instanceof Patient)) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Only patients can request appointments");
        }

        LogicResponse<Appointment> response = logic.requestAppointment((Patient) user.getPerson(), request);*/
        LogicResponse<Appointment> response = logic.requestAppointment(
                patientRepo.findById(request.getPatientId()).orElse(null), request);
        if (response.getBody() == null) {
            return ErrorResponse.of(response).asResponseEntity();
        }

        return ResponseEntity.ok(response.getBody());
    }

    @PostMapping("/create")
    public ResponseEntity createAppointment(
            //@ModelAttribute("user") User user,
            @RequestBody CreateAppointmentRequest request
    ) {
        /* if (!canAccessOffice(user, request.getOfficeId())) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Cannot access that office's data");
        } */

        LogicResponse<Appointment> response = logic.createAppointment(request);
        if (response.getBody() == null) {
            return ErrorResponse.of(response).asResponseEntity();
        }

        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping("/{id}")
    public ResponseEntity getAppointment(
            //@ModelAttribute("user") User user,
            @PathVariable Integer id) {
        LogicResponse<Appointment> response = logic.getAppointment(id);

        if (response.getBody() == null) {
            return ErrorResponse.of(response).asResponseEntity();
        }

        /*if (!canAccessOffice(user, response.getBody().getOffice().getId())) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Cannot access that office's data");
        }*/

        return ResponseEntity.ok(response.getBody());
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity approveAppointmentRequest(
            //@ModelAttribute("user") User user,
            @PathVariable Integer id
    ) {
        LogicResponse<Appointment> response = logic.getAppointment(id);

        if (response.getBody() == null) {
            return ErrorResponse.of(response).asResponseEntity();
        }

        /*if (!canAccessOffice(user, response.getBody().getOffice().getId())) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Cannot access that office's data");
        }*/

        response = logic.approveAppointment(response.getBody());
        if (response.getBody() == null) {
            return ErrorResponse.of(response).asResponseEntity();
        }

        return ResponseEntity.ok(response.getBody());
    }

    @PostMapping("/{id}/reschedule")
    public ResponseEntity rescheduleAppointment(
            //@ModelAttribute("user") User user,
            @PathVariable Integer id, @RequestBody RescheduleAppointmentRequest request
    ) {
        LogicResponse<Appointment> response = logic.getAppointment(id);

        if (response.getBody() == null) {
            return ErrorResponse.of(response).asResponseEntity();
        }

        /*if (!canAccessOffice(user, response.getBody().getOffice().getId())) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Cannot access that office's data");
        }*/

        response = logic.rescheduleAppointment(response.getBody(), request);
        if (response.getBody() == null) {
            return ErrorResponse.of(response).asResponseEntity();
        }

        return ResponseEntity.ok(response.getBody());
    }

    @PostMapping("/{id}/register_in_waiting")
    public ResponseEntity registerAppointmentInWaiting(
            //@ModelAttribute("user") User user,
            @PathVariable Integer id
    ) {
        LogicResponse<Appointment> response = logic.getAppointment(id);

        if (response.getBody() == null) {
            return ErrorResponse.of(response).asResponseEntity();
        }

        /*if (!canAccessOffice(user, response.getBody().getOffice().getId())) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Cannot access that office's data");
        }*/

        response = logic.registerInWaitingRoom(response.getBody());
        if (response.getBody() == null) {
            return ErrorResponse.of(response).asResponseEntity();
        }

        return ResponseEntity.ok(response.getBody());
    }

    @PostMapping("/{id}/register_in_progress")
    public ResponseEntity registerAppointmentInProgress(
            //@ModelAttribute("user") User user,
            @PathVariable Integer id
    ) {
        LogicResponse<Appointment> response = logic.getAppointment(id);

        if (response.getBody() == null) {
            return ErrorResponse.of(response).asResponseEntity();
        }

        /*if (!canAccessOffice(user, response.getBody().getOffice().getId())) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Cannot access that office's data");
        }*/

        response = logic.registerInProgress(response.getBody());
        if (response.getBody() == null) {
            return ErrorResponse.of(response).asResponseEntity();
        }

        return ResponseEntity.ok(response.getBody());
    }

    @PostMapping("/{id}/finalize")
    public ResponseEntity finalizeSession(
            //@ModelAttribute("user") User user,
            @PathVariable Integer id, @RequestBody EditAppointmentRequest request
    ) {
        LogicResponse<Appointment> response = logic.getAppointment(id);

        if (response.getBody() == null) {
            return ErrorResponse.of(response).asResponseEntity();
        }

        /*if (!canAccessOffice(user, response.getBody().getOffice().getId())) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Cannot access that office's data");
        }*/

        response = logic.finalizeSession(response.getBody(), request);
        if (response.getBody() == null) {
            return ErrorResponse.of(response).asResponseEntity();
        }

        return ResponseEntity.ok(response.getBody());
    }

    @PostMapping("/{id}/edit_session")
    public ResponseEntity editSession(
            //@ModelAttribute("user") User user,
            @PathVariable Integer id, @RequestBody EditAppointmentRequest request
    ) {
        LogicResponse<Appointment> response = logic.getAppointment(id);

        if (response.getBody() == null) {
            return ErrorResponse.of(response).asResponseEntity();
        }

        /*if (!canAccessOffice(user, response.getBody().getOffice().getId())) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Cannot access that office's data");
        }*/

        response = logic.editSession(response.getBody(), request);
        if (response.getBody() == null) {
            return ErrorResponse.of(response).asResponseEntity();
        }

        return ResponseEntity.ok(response.getBody());
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity cancelAppointment(
            //@ModelAttribute("user") User user,
            @PathVariable Integer id, @RequestBody CancelAppointmentRequest request
    ) {
        LogicResponse<Appointment> response = logic.getAppointment(id);

        if (response.getBody() == null) {
            return ErrorResponse.of(response).asResponseEntity();
        }

        /*if (!user.canAccessOffice(response.getBody().getOffice().getId())) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "Cannot access that office's data");
        }*/

        response = logic.cancelAppointment(response.getBody(), request);
        if (response.getBody() == null) {
            return ErrorResponse.of(response).asResponseEntity();
        }

        return ResponseEntity.ok(response.getBody());
    }

    private boolean canAccessOffice(User user, Integer officeId) {
        return user.getPerson() instanceof OfficeWorker && ((OfficeWorker) user.getPerson()).worksInOffice(officeId);
    }
}
