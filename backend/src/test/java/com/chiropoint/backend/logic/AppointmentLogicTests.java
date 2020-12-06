package com.chiropoint.backend.logic;

import com.chiropoint.backend.domain.models.Appointment;
import com.chiropoint.backend.domain.models.OfficeWorker;
import com.chiropoint.backend.domain.models.Subluxation;
import com.chiropoint.backend.domain.repositories.*;
import com.chiropoint.backend.dto.request.appointment.*;
import com.chiropoint.backend.dto.response.LogicResponse;
import com.chiropoint.backend.mocks.*;
import com.chiropoint.backend.service.MailingService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static com.chiropoint.backend.mocks.AppointmentTypeMock.EVAL;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppointmentLogic.class)
@ActiveProfiles("scratch")
public class AppointmentLogicTests {

    private static int OK = 0;
    private static int RESOURCE_MISSING = 40401;
    private static int RESOURCE_INVALID = 40402;
    private static int DATABASE_ERROR = 50001;
    private static int INTERNAL_MISMATCH_ERROR = 90001;

    @MockBean
    private AppointmentRepository apptRepoMock;
    @MockBean
    private AppointmentTypeRepository typeRepoMock;
    @MockBean
    private PatientRepository patientRepoMock;
    @MockBean
    private OfficeRepository officeRepoMock;
    @MockBean
    private OfficeWorkerRepository workerRepoMock;
    @MockBean
    private SubluxationRepository subRepoMock;

    @MockBean
    private MailingService mailServiceMock;

    @Autowired
    private AppointmentLogic apptLogic;

    private ArrayList<Appointment> appointments = AppointmentMock.appointments;

    @Before
    public void setUp() {
        AppointmentTypeMock.setUpRepository(typeRepoMock);
        OfficeMock.setUpRepository(officeRepoMock);
        OfficeWorkerMock.setUpRepository(workerRepoMock);
        PatientMock.setUpRepository(patientRepoMock);
        AppointmentMock.setUpRepository(apptRepoMock);

        PatientMock.addAppointments();
        OfficeWorkerMock.addOffices();

        when(apptRepoMock.findAllByOffice(OfficeMock.offices.get(0))).thenReturn(appointments);
        when(mailServiceMock.notifyAppointment(any(), any())).thenReturn(true);
    }

    /* ------ GET APPOINTMENTS ------ */

    @Test
    public void getAppointmentsShouldNotBeEmptyWhenAllExistsAndNoTimeIsGiven() {
        AppointmentQueryRequest request = new AppointmentQueryRequest(
            0, null, null, 0, 0
        );

        LogicResponse<List<Appointment>> response = apptLogic.getAppointments(request);

        assertEquals(response.getCode(), OK);
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());

        verify(officeRepoMock, times(1)).findById(any());
        verify(workerRepoMock, times(1)).findById(any());
    }

    @Test
    public void getAppointmentsShouldNotBeEmptyWhenAllExistsAndTimeIsGiven() {
        ZoneOffset offset = ZoneOffset.ofHours(0);
        Date from = Date.from(LocalDateTime.now().minusDays(1).toInstant(offset));
        Date to = Date.from(LocalDateTime.now().plusDays(1).toInstant(offset));

        AppointmentQueryRequest request = new AppointmentQueryRequest(
                0, from, to, 0, 0
        );

        LogicResponse<List<Appointment>> response = apptLogic.getAppointments(request);

        assertEquals(response.getCode(), OK);
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());

        verify(officeRepoMock, times(1)).findById(any());
        verify(workerRepoMock, times(1)).findById(any());
    }

    @Test
    public void getAppointmentsShouldBeNullWhenOfficeIdIsNotProvided() {
        AppointmentQueryRequest request = new AppointmentQueryRequest(
                null, null, null, 0, 0
        );

        LogicResponse<List<Appointment>> response = apptLogic.getAppointments(request);

        assertEquals(response.getCode(), RESOURCE_MISSING);
        assertNull(response.getBody());

        verify(officeRepoMock, never()).findById(any());
        verify(patientRepoMock, never()).findById(any());
        verify(workerRepoMock, never()).findById(any());
    }

    @Test
    public void getAppointmentsShouldBeNullWhenOfficeDoesntExist() {
        AppointmentQueryRequest request = new AppointmentQueryRequest(
                999, null, null, 0, 0
        );

        LogicResponse<List<Appointment>> response = apptLogic.getAppointments(request);

        assertEquals(response.getCode(), RESOURCE_INVALID);
        assertNull(response.getBody());

        verify(officeRepoMock, times(1)).findById(any());
        verify(patientRepoMock, never()).findById(any());
        verify(workerRepoMock, never()).findById(any());
    }

    @Test
    public void getAppointmentsShouldBeEmptyWhenOfficeHasNoAppointments() {
        AppointmentQueryRequest request = new AppointmentQueryRequest(
                1, null, null, 0, 0
        );

        LogicResponse<List<Appointment>> response = apptLogic.getAppointments(request);

        assertEquals(response.getCode(), OK);
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(officeRepoMock, times(1)).findById(any());
        verify(patientRepoMock, never()).findById(any());
        verify(workerRepoMock, never()).findById(any());
    }

    @Test
    public void getAppointmentsShouldBeEmptyWhenPatientHasNoAppointments() {
        AppointmentQueryRequest request = new AppointmentQueryRequest(
                0, null, null, 0, 1
        );

        LogicResponse<List<Appointment>> response = apptLogic.getAppointments(request);

        assertEquals(response.getCode(), OK);
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(officeRepoMock, times(1)).findById(any());
        verify(patientRepoMock, times(1)).findById(any());
        verify(workerRepoMock, times(1)).findById(any());
    }

    @Test
    public void getAppointmentsShouldBeEmptyWhenChiropractorHasNoAppointments() {
        AppointmentQueryRequest request = new AppointmentQueryRequest(
                0, null, null, 1, 0
        );

        LogicResponse<List<Appointment>> response = apptLogic.getAppointments(request);

        assertEquals(response.getCode(), OK);
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(officeRepoMock, times(1)).findById(any());
        verify(patientRepoMock, times(1)).findById(any());
        verify(workerRepoMock, times(1)).findById(any());
    }

    /* ------ REQUEST APPOINTMENTS ------ */

    @Test
    public void requestAppointmentShouldSaveTheAppointmentCorrectlyIfRequestIsOk() {
        Date date = new Date();
        RequestAppointmentRequest request = new RequestAppointmentRequest(
                1, 2, date, 0, 0
        );

        ArgumentCaptor<Appointment> apptCaptor = ArgumentCaptor.forClass(Appointment.class);
        when(apptRepoMock.save(apptCaptor.capture())).thenAnswer(i -> apptCaptor.getValue());

        LogicResponse<Appointment> response = apptLogic.requestAppointment(PatientMock.patients.get(1), request);
        assertEquals(response.getCode(), OK);
        assertNotNull(response.getBody());

        Appointment appointment = response.getBody();

        assertEquals(appointment.getType(), EVAL);
        assertEquals(appointment.getDateTime(), date);
        assertEquals(appointment.getPreviousDateTime(), date);
        assertEquals(appointment.getStatus(), Appointment.Status.REQUESTED);
        assertEquals(appointment.getOffice(), OfficeMock.offices.get(0));
        assertEquals(appointment.getPatient(), PatientMock.patients.get(1));
        assertEquals(appointment.getChiropractor(), OfficeWorkerMock.workers.get(0));

        verify(officeRepoMock, times(1)).findById(any());
        verify(workerRepoMock, times(1)).findById(any());
        verify(apptRepoMock, times(1)).save(any());
    }

    @Test
    public void requestAppointmentShouldReturnNullIfTheChiropractorDoesntBelongToTheOffice() {
        Date date = new Date();
        RequestAppointmentRequest request = new RequestAppointmentRequest(
                1, 2, date, 0, 1
        );

        when(apptRepoMock.save(any())).thenReturn(null);

        LogicResponse<Appointment> response = apptLogic.requestAppointment(PatientMock.patients.get(0), request);

        assertEquals(response.getCode(), INTERNAL_MISMATCH_ERROR);
        assertNull(response.getBody());

        verify(officeRepoMock, times(1)).findById(any());
        verify(workerRepoMock, times(1)).findById(any());
        verify(apptRepoMock, never()).save(any());

    }

    @Test
    public void requestAppointmentShouldReturnNullIfPatientIsNull() {
        Date date = new Date();
        RequestAppointmentRequest request = new RequestAppointmentRequest(
                1, 2, date, 0, 0
        );

        when(apptRepoMock.save(any())).thenReturn(null);

        LogicResponse<Appointment> response = apptLogic.requestAppointment(null, request);

        assertEquals(response.getCode(), RESOURCE_MISSING);
        assertNull(response.getBody());

        verify(officeRepoMock, never()).findById(any());
        verify(workerRepoMock, never()).findById(any());
        verify(apptRepoMock, never()).save(any());

    }

    @Test
    public void requestAppointmentShouldReturnNullIfOfficeIdIsInvalid() {
        Date date = new Date();
        RequestAppointmentRequest request = new RequestAppointmentRequest(
                1, 2, date, 999, 0
        );

        when(apptRepoMock.save(any())).thenReturn(null);

        LogicResponse<Appointment> response = apptLogic.requestAppointment(PatientMock.patients.get(1), request);

        assertEquals(response.getCode(), RESOURCE_INVALID);
        assertNull(response.getBody());

        verify(officeRepoMock, times(1)).findById(any());
        verify(workerRepoMock, never()).findById(any());
        verify(apptRepoMock, never()).save(any());
    }

    @Test
    public void requestAppointmentShouldReturnNullIfChiropractorIdIsInvalid() {
        Date date = new Date();
        RequestAppointmentRequest request = new RequestAppointmentRequest(
                1, 2, date, 0, 999
        );

        when(apptRepoMock.save(any())).thenReturn(null);

        LogicResponse<Appointment> response = apptLogic.requestAppointment(PatientMock.patients.get(1), request);

        assertEquals(response.getCode(), RESOURCE_INVALID);
        assertNull(response.getBody());

        verify(officeRepoMock, times(1)).findById(any());
        verify(workerRepoMock, times(1)).findById(any());
        verify(apptRepoMock, never()).save(any());
    }

    /* ------ APPROVE APPOINTMENT ------ */

    @Test
    public void approveAppointmentShouldChangeTheAppointmentStatus() {
        ArgumentCaptor<Appointment> apptCaptor = ArgumentCaptor.forClass(Appointment.class);
        when(apptRepoMock.save(apptCaptor.capture())).thenAnswer(i -> apptCaptor.getValue());

        Appointment.Status oldStatus = AppointmentMock.appointments.get(0).getStatus();

        LogicResponse<Appointment> response = apptLogic.approveAppointment(AppointmentMock.appointments.get(0));
        assertEquals(response.getCode(), OK);
        assertNotNull(response.getBody());
        assertEquals(response.getBody().getStatus(), Appointment.Status.PENDING);

        verify(apptRepoMock, times(1)).save(any());

        // Reset status to previous one just in case.
        response.getBody().setStatus(oldStatus);
    }

    @Test
    public void approveAppointmentShouldReturnNullIfTheAppointmentIdIsInvalid() {
        LogicResponse<Appointment> response = apptLogic.approveAppointment(null);

        assertEquals(response.getCode(), RESOURCE_INVALID);
        assertNull(response.getBody());

        verify(apptRepoMock, never()).save(any());
    }


    /* ------ CREATE APPOINTMENT ------ */

    @Test
    public void createAppointmentShouldSaveTheAppointmentCorrectlyIfRequestIsOk() {
        Date date = new Date();
        CreateAppointmentRequest request = new CreateAppointmentRequest(
                EVAL.getId(), date, 0, 0, 0
        );

        ArgumentCaptor<Appointment> apptCaptor = ArgumentCaptor.forClass(Appointment.class);
        when(apptRepoMock.save(apptCaptor.capture())).thenAnswer(i -> {
            Appointment appt = apptCaptor.getValue();
            appt.setId(1000);
            return appt;
        });

        LogicResponse<Appointment> response = apptLogic.createAppointment(request);
        assertEquals(response.getCode(), OK);
        assertNotNull(response.getBody());

        Appointment appointment = response.getBody();

        assertEquals(appointment.getType(), EVAL);
        assertEquals(appointment.getDateTime(), date);
        assertEquals(appointment.getPreviousDateTime(), date);
        assertEquals(appointment.getStatus(), Appointment.Status.PENDING);
        assertEquals(appointment.getOffice(), OfficeMock.offices.get(0));
        assertEquals(appointment.getPatient(), PatientMock.patients.get(0));
        assertEquals(appointment.getChiropractor(), OfficeWorkerMock.workers.get(0));

        verify(typeRepoMock, times(1)).findById(any());
        verify(patientRepoMock, times(1)).findById(any());
        verify(officeRepoMock, times(1)).findById(any());
        verify(workerRepoMock, times(1)).findById(any());
        verify(apptRepoMock, times(1)).save(any());
        verify(mailServiceMock, times(1)).notifyAppointment(any(), any());
    }

    @Test
    public void createAppointmentShouldFailIfTheChiropractorDoesntBelongToTheOffice() {
        Date date = new Date();
        CreateAppointmentRequest request = new CreateAppointmentRequest(
                EVAL.getId(), date, 0, 0, 1
        );

        ArgumentCaptor<Appointment> apptCaptor = ArgumentCaptor.forClass(Appointment.class);
        when(apptRepoMock.save(apptCaptor.capture())).thenAnswer(i -> apptCaptor.getValue());

        LogicResponse<Appointment> response = apptLogic.createAppointment(request);
        assertEquals(response.getCode(), INTERNAL_MISMATCH_ERROR);
        assertNull(response.getBody());

        verify(typeRepoMock, times(1)).findById(any());
        verify(patientRepoMock, times(1)).findById(any());
        verify(officeRepoMock, times(1)).findById(any());
        verify(workerRepoMock, times(1)).findById(any());
        verify(apptRepoMock, never()).save(any());
        verify(mailServiceMock, never()).notifyAppointment(any(), any());
    }

    @Test
    public void createAppointmentShouldFailIfTheSaveMethodFails() {
        Date date = new Date();
        CreateAppointmentRequest request = new CreateAppointmentRequest(
                EVAL.getId(), date, 0, 0, 0
        );

        ArgumentCaptor<Appointment> apptCaptor = ArgumentCaptor.forClass(Appointment.class);
        when(apptRepoMock.save(apptCaptor.capture())).thenAnswer(i -> apptCaptor.getValue());

        LogicResponse<Appointment> response = apptLogic.createAppointment(request);
        assertEquals(response.getCode(), DATABASE_ERROR);
        assertNull(response.getBody());

        verify(typeRepoMock, times(1)).findById(any());
        verify(patientRepoMock, times(1)).findById(any());
        verify(officeRepoMock, times(1)).findById(any());
        verify(workerRepoMock, times(1)).findById(any());
        verify(apptRepoMock, times(1)).save(any());
        verify(mailServiceMock, never()).notifyAppointment(any(), any());
    }

    @Test
    public void createAppointmentShouldReturnNullIfTypeIsInvalid() {
        Date date = new Date();
        CreateAppointmentRequest request = new CreateAppointmentRequest(
                999, date, 0, 999, 0
        );

        when(apptRepoMock.save(any())).thenReturn(null);

        LogicResponse<Appointment> response = apptLogic.createAppointment(request);
        assertEquals(response.getCode(), RESOURCE_INVALID);
        assertNull(response.getBody());
        assertNotNull(response.getError());

        verify(typeRepoMock, times(1)).findById(any());
        verify(patientRepoMock, never()).findById(any());
        verify(officeRepoMock, never()).findById(any());
        verify(workerRepoMock, never()).findById(any());
        verify(apptRepoMock, never()).save(any());
        verify(mailServiceMock, never()).notifyAppointment(any(), any());
    }

    @Test
    public void createAppointmentShouldReturnNullIfPatientIdIsInvalid() {
        Date date = new Date();
        CreateAppointmentRequest request = new CreateAppointmentRequest(
                EVAL.getId(), date, 0, 999, 0
        );

        when(apptRepoMock.save(any())).thenReturn(null);

        LogicResponse<Appointment> response = apptLogic.createAppointment(request);
        assertEquals(response.getCode(), RESOURCE_INVALID);
        assertNull(response.getBody());
        assertNotNull(response.getError());

        verify(typeRepoMock, times(1)).findById(any());
        verify(patientRepoMock, times(1)).findById(any());
        verify(officeRepoMock, never()).findById(any());
        verify(workerRepoMock, never()).findById(any());
        verify(apptRepoMock, never()).save(any());
        verify(mailServiceMock, never()).notifyAppointment(any(), any());
    }

    @Test
    public void createAppointmentShouldReturnNullIfOfficeIdIsInvalid() {
        Date date = new Date();
        CreateAppointmentRequest request = new CreateAppointmentRequest(
                EVAL.getId(), date, 999, 0, 0
        );

        when(apptRepoMock.save(any())).thenReturn(null);

        LogicResponse<Appointment> response = apptLogic.createAppointment(request);
        assertEquals(response.getCode(), RESOURCE_INVALID);
        assertNull(response.getBody());
        assertNotNull(response.getError());

        verify(typeRepoMock, times(1)).findById(any());
        verify(patientRepoMock, times(1)).findById(any());
        verify(officeRepoMock, times(1)).findById(any());
        verify(workerRepoMock, never()).findById(any());
        verify(apptRepoMock, never()).save(any());
        verify(mailServiceMock, never()).notifyAppointment(any(), any());
    }

    @Test
    public void createAppointmentShouldReturnNullIfChiropractorIdIsInvalid() {
        Date date = new Date();
        CreateAppointmentRequest request = new CreateAppointmentRequest(
                EVAL.getId(), date, 0, 0, 999
        );

        when(apptRepoMock.save(any())).thenReturn(null);

        LogicResponse<Appointment> response = apptLogic.createAppointment(request);
        assertEquals(response.getCode(), RESOURCE_INVALID);
        assertNull(response.getBody());
        assertNotNull(response.getError());

        verify(typeRepoMock, times(1)).findById(any());
        verify(patientRepoMock, times(1)).findById(any());
        verify(officeRepoMock, times(1)).findById(any());
        verify(workerRepoMock, times(1)).findById(any());
        verify(apptRepoMock, never()).save(any());
        verify(mailServiceMock, never()).notifyAppointment(any(), any());
    }

    /* ------ RESCHEDULE APPOINTMENT ------ */

    @Test
    public void rescheduleAppointmentShouldChangeTheDateAndStatusIfEverythingIsOk() {
        Date newDate = Date.from(LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.ofHours(0)));

        RescheduleAppointmentRequest request = new RescheduleAppointmentRequest(newDate);

        ArgumentCaptor<Appointment> apptCaptor = ArgumentCaptor.forClass(Appointment.class);
        when(apptRepoMock.save(apptCaptor.capture())).thenAnswer(i ->  apptCaptor.getValue());

        Date previousDate = appointments.get(0).getDateTime();
        Appointment.Status previousStatus = appointments.get(0).getStatus();

        LogicResponse<Appointment> response = apptLogic.rescheduleAppointment(appointments.get(0), request);
        assertEquals(response.getCode(), OK);
        assertNotNull(response.getBody());

        Appointment appointment = response.getBody();

        assertEquals(appointment.getDateTime(), newDate);
        assertEquals(appointment.getPreviousDateTime(), previousDate);
        assertEquals(appointment.getStatus(), Appointment.Status.RESCHEDULED);

        verify(apptRepoMock, times(1)).save(any());

        // Reset time and status to previous one just in case.
        appointment.setDateTime(previousDate);
        appointment.setStatus(previousStatus);
    }

    @Test
    public void rescheduleAppointmentShouldDoNothingIfDateIsNull() {
        RescheduleAppointmentRequest request = new RescheduleAppointmentRequest(null);

        LogicResponse<Appointment> response = apptLogic.rescheduleAppointment(null, request);

        assertEquals(response.getCode(), RESOURCE_MISSING);
        assertNull(response.getBody());

        verify(apptRepoMock, never()).findById(any());
        verify(apptRepoMock, never()).save(any());
    }

    @Test
    public void rescheduleAppointmentShouldDoNothingIfDateIsBeforeNow() {
        RescheduleAppointmentRequest request = new RescheduleAppointmentRequest(new Date());

        LogicResponse<Appointment> response = apptLogic.rescheduleAppointment(null, request);

        assertEquals(response.getCode(), RESOURCE_INVALID);
        assertNull(response.getBody());

        verify(apptRepoMock, never()).findById(any());
        verify(apptRepoMock, never()).save(any());
    }

    @Test
    public void rescheduleAppointmentShouldDoNothingIfAppointmentIdIsInvalid() {
        RescheduleAppointmentRequest request = new RescheduleAppointmentRequest(
                Date.from(LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.ofHours(0)))
        );

        LogicResponse<Appointment> response = apptLogic.rescheduleAppointment(null, request);

        assertEquals(response.getCode(), RESOURCE_INVALID);
        assertNull(response.getBody());

        verify(apptRepoMock, never()).save(any());
    }

    /* ------ REGISTER IN WAITING ROOM ------ */

    @Test
    public void registerInWaitingRoomShouldChangeTheStatusIfEverythingIsOk() {
        ArgumentCaptor<Appointment> apptCaptor = ArgumentCaptor.forClass(Appointment.class);
        when(apptRepoMock.save(apptCaptor.capture())).thenAnswer(i ->  apptCaptor.getValue());

        Appointment.Status oldStatus = appointments.get(0).getStatus();

        LogicResponse<Appointment> response = apptLogic.registerInWaitingRoom(appointments.get(0));
        assertEquals(response.getCode(), OK);
        assertNotNull(response.getBody());

        Appointment appointment = response.getBody();

        assertEquals(appointment.getStatus(), Appointment.Status.IN_WAITING_ROOM);

        verify(apptRepoMock, times(1)).save(any());

        // Reset time and status to previous one just in case.
        appointment.setStatus(oldStatus);
    }

    @Test
    public void registerInWaitingRoomShouldDoNothingIfTheAppointmentIdIsInvalid() {
        when(apptRepoMock.save(any())).thenReturn(null);

        LogicResponse<Appointment> response = apptLogic.registerInWaitingRoom(null);
        assertEquals(response.getCode(), RESOURCE_INVALID);
        assertNull(response.getBody());

        verify(apptRepoMock, never()).save(any());
    }

    /* ------ REGISTER IN PROGRESS ------ */

    @Test
    public void registerInProgressShouldChangeTheStatusIfEverythingIsOk() {
        ArgumentCaptor<Appointment> apptCaptor = ArgumentCaptor.forClass(Appointment.class);
        when(apptRepoMock.save(apptCaptor.capture())).thenAnswer(i ->  apptCaptor.getValue());

        Appointment.Status oldStatus = appointments.get(0).getStatus();

        LogicResponse<Appointment> response = apptLogic.registerInProgress(appointments.get(0));
        assertEquals(response.getCode(), OK);
        assertNotNull(response.getBody());

        Appointment appointment = response.getBody();

        assertEquals(appointment.getStatus(), Appointment.Status.IN_PROGRESS);

        verify(apptRepoMock, times(1)).save(any());

        // Reset time and status to previous one just in case.
        appointment.setStatus(oldStatus);
    }

    @Test
    public void registerInProgressShouldDoNothingIfTheAppointmentIdIsInvalid() {
        when(apptRepoMock.save(any())).thenReturn(null);

        LogicResponse<Appointment> response = apptLogic.registerInWaitingRoom(null);
        assertEquals(response.getCode(), RESOURCE_INVALID);
        assertNull(response.getBody());

        verify(apptRepoMock, never()).save(any());
    }

    /* ------ EDIT SESSION ------ */

    @Test
    public void editSessionShouldChangeTheReasonAndSubluxationsIfEverythingIsOk() {
        EditAppointmentRequest request = new EditAppointmentRequest("reason", new LinkedList<>());
        request.getSubluxations().add(new EditAppointmentRequest.SubluxationDto('S', 1, 2));
        request.getSubluxations().add(new EditAppointmentRequest.SubluxationDto('L', 5, 3));

        ArgumentCaptor<Subluxation> subCaptor = ArgumentCaptor.forClass(Subluxation.class);
        when(subRepoMock.save(subCaptor.capture())).thenAnswer(i ->  subCaptor.getValue());
        ArgumentCaptor<Appointment> apptCaptor = ArgumentCaptor.forClass(Appointment.class);
        when(apptRepoMock.save(apptCaptor.capture())).thenAnswer(i ->  apptCaptor.getValue());

        LogicResponse<Appointment> response = apptLogic.editSession(appointments.get(0), request);
        assertEquals(response.getCode(), OK);
        assertNotNull(response.getBody());

        Appointment appointment = response.getBody();

        assertEquals(appointment.getDescription(), "reason");
        for (int idx = 0; idx < request.getSubluxations().size(); idx++) {
            assertEquals(appointment.getSubluxations().get(idx).getZone(),
                    request.getSubluxations().get(idx).getZone());
            assertEquals(appointment.getSubluxations().get(idx).getVertebra(),
                    request.getSubluxations().get(idx).getVertebra());
            assertEquals(appointment.getSubluxations().get(idx).getSeverity(),
                    request.getSubluxations().get(idx).getSeverity());
        }

        verify(subRepoMock, times(request.getSubluxations().size())).save(any());
        verify(apptRepoMock, times(1)).save(any());
    }

    @Test
    public void editSessionShouldDoNothingIfASubluxationIsInvalid() {
        EditAppointmentRequest request = new EditAppointmentRequest("reason", new LinkedList<>());
        request.getSubluxations().add(new EditAppointmentRequest.SubluxationDto('C', 10, 2));

        when(subRepoMock.save(any())).thenReturn(null);
        when(apptRepoMock.save(any())).thenReturn(null);

        LogicResponse<Appointment> response = apptLogic.editSession(appointments.get(0), request);
        assertEquals(response.getCode(), RESOURCE_INVALID);
        assertNull(response.getBody());

        verify(subRepoMock, never()).save(any());
        verify(apptRepoMock, never()).save(any());
    }

    @Test
    public void editSessionShouldDoNothingIfTheAppointmentIdIsInvalid() {
        EditAppointmentRequest request = new EditAppointmentRequest( "reason", null);

        when(apptRepoMock.save(any())).thenReturn(null);

        LogicResponse<Appointment> response = apptLogic.editSession(null, request);
        assertEquals(response.getCode(), RESOURCE_INVALID);
        assertNull(response.getBody());

        verify(apptRepoMock, never()).save(any());
    }

    /* ------ FINALIZE SESSION ------ */

    @Test
    public void finalizeSessionShouldChangeTheStatusReasonAndSubluxationsIfEverythingIsOk() {
        EditAppointmentRequest request = new EditAppointmentRequest("reason", new LinkedList<>());
        request.getSubluxations().add(new EditAppointmentRequest.SubluxationDto('C', 2, 0));
        request.getSubluxations().add(new EditAppointmentRequest.SubluxationDto('D', 10, 1));
        request.getSubluxations().add(new EditAppointmentRequest.SubluxationDto('L', 5, 2));
        request.getSubluxations().add(new EditAppointmentRequest.SubluxationDto('S', 1, 3));

        ArgumentCaptor<Subluxation> subCaptor = ArgumentCaptor.forClass(Subluxation.class);
        when(subRepoMock.save(subCaptor.capture())).thenAnswer(i ->  subCaptor.getValue());
        ArgumentCaptor<Appointment> apptCaptor = ArgumentCaptor.forClass(Appointment.class);
        when(apptRepoMock.save(apptCaptor.capture())).thenAnswer(i ->  apptCaptor.getValue());

        Appointment.Status oldStatus = appointments.get(1).getStatus();

        LogicResponse<Appointment> response = apptLogic.finalizeSession(appointments.get(0), request);
        assertEquals(response.getCode(), OK);
        assertNotNull(response.getBody());

        Appointment appointment = response.getBody();

        assertNotNull(appointment);
        assertEquals(appointment.getStatus(), Appointment.Status.FINALIZED);
        assertEquals(appointment.getDescription(), "reason");
        for (int idx = 0; idx < request.getSubluxations().size(); idx++) {
            assertEquals(appointment.getSubluxations().get(idx).getZone(),
                    request.getSubluxations().get(idx).getZone());
            assertEquals(appointment.getSubluxations().get(idx).getVertebra(),
                    request.getSubluxations().get(idx).getVertebra());
            assertEquals(appointment.getSubluxations().get(idx).getSeverity(),
                    request.getSubluxations().get(idx).getSeverity());
        }

        verify(subRepoMock, times(request.getSubluxations().size())).save(any());
        verify(apptRepoMock, times(1)).save(any());

        // Reset time and status to previous one just in case.
        appointment.setStatus(oldStatus);
    }

    @Test
    public void finalizeSessionShouldDoNothingIfASubluxationIsInvalid() {
        EditAppointmentRequest request = new EditAppointmentRequest("reason", new LinkedList<>());
        request.getSubluxations().add(new EditAppointmentRequest.SubluxationDto('C', 10, 2));

        when(subRepoMock.save(any())).thenReturn(null);
        when(apptRepoMock.save(any())).thenReturn(null);

        LogicResponse<Appointment> response = apptLogic.editSession(appointments.get(0), request);
        assertEquals(response.getCode(), RESOURCE_INVALID);
        assertNull(response.getBody());

        verify(subRepoMock, never()).save(any());
        verify(apptRepoMock, never()).save(any());
    }

    @Test
    public void finalizeSessionShouldDoNothingIfTheAppointmentIdIsInvalid() {
        EditAppointmentRequest request = new EditAppointmentRequest("reason", null);

        when(apptRepoMock.save(any())).thenReturn(null);

        LogicResponse<Appointment> response = apptLogic.editSession(null, request);
        assertEquals(response.getCode(), RESOURCE_INVALID);
        assertNull(response.getBody());

        verify(apptRepoMock, never()).save(any());
    }

    /* ------ CANCEL APPOINTMENT ------ */

    @Test
    public void cancelAppointmentShouldChangeTheStatusAndDescriptionIfEverythingIsOk() {
        CancelAppointmentRequest request = new CancelAppointmentRequest("reason");

        ArgumentCaptor<Appointment> apptCaptor = ArgumentCaptor.forClass(Appointment.class);
        when(apptRepoMock.save(apptCaptor.capture())).thenAnswer(i ->  apptCaptor.getValue());

        Appointment.Status oldStatus = appointments.get(0).getStatus();

        LogicResponse<Appointment> response = apptLogic.cancelAppointment(appointments.get(0), request);
        assertEquals(response.getCode(), OK);
        assertNotNull(response.getBody());

        Appointment appointment = response.getBody();

        assertNotNull(appointment);
        assertEquals(appointment.getStatus(), Appointment.Status.CANCELLED);
        assertEquals(appointment.getDescription(), "reason");

        verify(apptRepoMock, times(1)).save(any());

        // Reset time and status to previous one just in case.
        appointment.setStatus(oldStatus);
    }

    @Test
    public void cancelAppointmentShouldDoNothingIfTheAppointmentIdIsInvalid() {
        CancelAppointmentRequest request = new CancelAppointmentRequest("reason");

        when(apptRepoMock.save(any())).thenReturn(null);

        LogicResponse<Appointment> response = apptLogic.cancelAppointment(null, request);
        assertEquals(response.getCode(), RESOURCE_INVALID);
        assertNull(response.getBody());

        verify(apptRepoMock, never()).save(any());
    }

}
