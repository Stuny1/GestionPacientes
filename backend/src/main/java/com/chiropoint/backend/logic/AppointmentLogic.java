package com.chiropoint.backend.logic;

import com.chiropoint.backend.domain.models.*;
import com.chiropoint.backend.domain.repositories.*;
import com.chiropoint.backend.dto.request.appointment.*;
import com.chiropoint.backend.dto.response.LogicResponse;
import com.chiropoint.backend.service.MailingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AppointmentLogic {

    @Autowired
    private MailingService mailingService;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentTypeRepository appointmentTypeRepository;

    @Autowired
    private SubluxationRepository subluxationRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private OfficeWorkerRepository officeWorkerRepository;

    @Autowired
    private OfficeRepository officeRepository;

    public LogicResponse<Appointment> getAppointment(Integer appointmentId) { // TODO - tests
        if (appointmentId == null) {
            return new LogicResponse<>(40401, "No appointment ID provided");
        }

        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);

        if (appointment == null) {
            return new LogicResponse<>(40402, "Invalid appointment ID");
        }

        return new LogicResponse<>(appointment);
    }

    public LogicResponse<List<Appointment>> getAppointments(AppointmentQueryRequest queryData) {
        if (queryData.getOfficeId() == null) {
            return new LogicResponse<>(40401, "No office ID provided");
        }

        Office office = officeRepository.findById(queryData.getOfficeId()).orElse(null);

        if (office == null) {
            return new LogicResponse<>(40402, "Invalid office ID");
        }

        List<Appointment> appointments = appointmentRepository.findAllByOffice(office);

        if (appointments.isEmpty()) {
            return new LogicResponse<>(appointments);
        }

        Integer patientId = queryData.getPatientId();
        Integer chiroId = queryData.getChiropractorId();
        Patient patient = patientId == null ? null : patientRepository.findById(queryData.getPatientId()).orElse(null);
        OfficeWorker chiropractor = chiroId == null ? null : officeWorkerRepository
                .findById(queryData.getChiropractorId()).orElse(null);

        appointments = appointments.stream().filter(
            appointment -> {
                final boolean timeCheck = (queryData.getFrom() == null || queryData.getTo() == null)
                        || appointment.getDateTime().after(queryData.getFrom()) &&
                        appointment.getDateTime().before(queryData.getTo());

                final boolean patientCheck = patient == null || appointment.getPatient() != null &&
                        appointment.getPatient().getId().equals(patient.getId());

                final boolean chiropractorCheck = chiropractor == null || appointment.getChiropractor() != null &&
                        appointment.getChiropractor().getId().equals(chiropractor.getId());

                return timeCheck && patientCheck && chiropractorCheck;
            }).collect(Collectors.toList());

        return new LogicResponse<>(appointments);
    }

    public LogicResponse<Appointment> requestAppointment(Patient patient, RequestAppointmentRequest requestData) {
        if (patient == null) {
            return new LogicResponse<>(40401, "No patient provided");
        }

        AppointmentType type = appointmentTypeRepository.findById(requestData.getTypeId()).orElse(null);
        if (type == null) {
            return new LogicResponse<>(40402, "Invalid appointment type ID");
        }

        Office office = officeRepository.findById(requestData.getOfficeId()).orElse(null);
        if (office == null) {
            return new LogicResponse<>(40402, "Invalid office ID");
        }

        OfficeWorker chiropractor = officeWorkerRepository.findById(requestData.getChiropractorId()).orElse(null);
        if (chiropractor == null) {
            return new LogicResponse<>(40402, "Invalid chiropractor ID");
        }

        if (!chiropractor.getOffices().contains(office)) {
            return new LogicResponse<>(90001, "Given chiropractor does not work in given office");
        }

        return new LogicResponse<>(appointmentRepository.save(
                newAppointment(
                        type, requestData.getDateTime(),
                        office, patient, chiropractor
                )
        ));
    }

    public LogicResponse<Appointment> createAppointment(CreateAppointmentRequest createData) {
        AppointmentType type = appointmentTypeRepository.findById(createData.getTypeId()).orElse(null);
        if (type == null) {
            return new LogicResponse<>(40402, "Invalid appointment type ID");
        }

        Patient patient = patientRepository.findById(createData.getPatientId()).orElse(null);
        if (patient == null) {
            return new LogicResponse<>(40402, "Invalid patient ID");
        }

        Office office = officeRepository.findById(createData.getOfficeId()).orElse(null);
        if (office == null) {
            return new LogicResponse<>(40402, "Invalid office ID");
        }

        OfficeWorker chiropractor = officeWorkerRepository.findById(createData.getChiropractorId()).orElse(null);
        if (chiropractor == null) {
            return new LogicResponse<>(40402, "Invalid chiropractor ID");
        }

        if (!chiropractor.getOffices().contains(office)) {
            return new LogicResponse<>(90001, "Given chiropractor does not work in given office");
            //todo 400 not 900
        }

        Appointment appointment = newAppointment(
                type, createData.getDateTime(),
                office, patient, chiropractor
        );
        appointment.setStatus(Appointment.Status.PENDING);

        appointment = appointmentRepository.save(appointment);

        if (appointment.getId() == null) {
            return new LogicResponse<>(50001, "Database error");
        }

        mailingService.notifyAppointment(patient, appointment);

        return new LogicResponse<>(appointment);
    }

    public LogicResponse<Appointment> approveAppointment(Appointment appointment) {
        if (appointment == null) {
            return new LogicResponse<>(40402, "Invalid appointment");
        }

        appointment.setStatus(Appointment.Status.PENDING);
        return new LogicResponse<>(appointmentRepository.save(appointment));
    }

    public LogicResponse<Appointment> rescheduleAppointment(
            Appointment appointment, RescheduleAppointmentRequest rescheduleData
    ) {

        if (rescheduleData.getDateTime() == null) {
            return new LogicResponse<>(40401, "No date provided");
        }

        if (appointment == null) {
            return new LogicResponse<>(40402, "Invalid appointment");
        }

        if (rescheduleData.getDateTime().before(new Date()) || rescheduleData.getDateTime().equals(new Date())) {
            return new LogicResponse<>(40402, "Invalid date provided - must be in the future");
        }

        appointment.setPreviousDateTime(appointment.getDateTime());
        appointment.setDateTime(rescheduleData.getDateTime());
        appointment.setStatus(Appointment.Status.RESCHEDULED);

        return new LogicResponse<>(appointmentRepository.save(appointment));
    }

    public LogicResponse<Appointment> registerInWaitingRoom(Appointment appointment) {
        return setStatus(appointment, Appointment.Status.IN_WAITING_ROOM);
    }

    public LogicResponse<Appointment> registerInProgress(Appointment appointment) {
        return setStatus(appointment, Appointment.Status.IN_PROGRESS);
    }

    public LogicResponse<Appointment> finalizeSession(Appointment appointment, EditAppointmentRequest editData) {
        if (appointment == null) {
            return new LogicResponse<>(40402, "Invalid appointment");
        }

        try {
            setDescriptionAndSubluxations(appointment, editData);
        } catch (InstantiationException ex) {
            return new LogicResponse<>(40402, "Invalid subluxation data provided");
        }

        appointment.setStatus(Appointment.Status.FINALIZED);
        return new LogicResponse<>(appointmentRepository.save(appointment));
    }

    public LogicResponse<Appointment> editSession(Appointment appointment, EditAppointmentRequest editData) {
        if (appointment == null) {
            return new LogicResponse<>(40402, "Invalid appointment");
        }

        try {
            setDescriptionAndSubluxations(appointment, editData);
        } catch (InstantiationException ex) {
            return new LogicResponse<>(40402, "Invalid subluxation data provided");
        }

        return new LogicResponse<>(appointmentRepository.save(appointment));
    }

    public LogicResponse<Appointment> cancelAppointment(Appointment appointment, CancelAppointmentRequest cancelData) {
        if (appointment == null) {
            return new LogicResponse<>(40402, "Invalid appointment");
        }

        appointment.setStatus(Appointment.Status.CANCELLED);
        appointment.setDescription(cancelData.getReason());

        return new LogicResponse<>(appointmentRepository.save(appointment));
    }

    private Appointment newAppointment(
            AppointmentType type, Date dateTime, Office office, Patient patient, OfficeWorker chiropractor
    ) {
        // TODO - If saving the session with a patient ID does not add the appointment to the patient's list,
        // then it will need to be done manually
        return new Appointment(type, dateTime, office, patient, chiropractor);
    }

    private LogicResponse<Appointment> setStatus(Appointment appointment, Appointment.Status status) {
        if (appointment == null) {
            return new LogicResponse<>(40402, "Invalid appointment ID");
        }

        appointment.setStatus(status);
        return new LogicResponse<>(appointmentRepository.save(appointment));
    }

    private void setDescriptionAndSubluxations(Appointment appointment, EditAppointmentRequest editData)
            throws InstantiationException {
        LinkedList<Subluxation> subluxations = new LinkedList<>();
        for (EditAppointmentRequest.SubluxationDto data : editData.getSubluxations()) {
            subluxations.add(
                    subluxationRepository.save(
                                new Subluxation(data.getZone(), data.getVertebra(), data.getSeverity())
                    )
            );
        }

        appointment.setDescription(editData.getDescription());

        deleteSubluxations(appointment);
        appointment.setSubluxations(subluxations);
    }

    private void deleteSubluxations(Appointment appointment) {
        subluxationRepository.deleteAll(appointment.getSubluxations());
    }

}
