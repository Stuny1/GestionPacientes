package com.chiropoint.backend.controllers;

import com.chiropoint.backend.controllers.handlers.ErrorResponse;
import com.chiropoint.backend.domain.models.OfficeWorker;
import com.chiropoint.backend.domain.models.Statistic;
import com.chiropoint.backend.domain.repositories.OfficeWorkerRepository;
import com.chiropoint.backend.dto.request.office.StatisticModifyRequest;
import com.chiropoint.backend.dto.request.office.StatisticsQueryRequest;
import com.chiropoint.backend.dto.response.LogicResponse;
import com.chiropoint.backend.logic.OfficeLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/offices")
public class OfficeController {

    @Autowired
    private OfficeLogic logic;
    @Autowired
    private OfficeWorkerRepository workerRepository;

    @GetMapping("/{id}/chiropractors")
    public ResponseEntity getOfficeChiropractors(@PathVariable("id") Integer officeId) {

        //ControllerUtil.checkUserOfficeAccess(user, officeId);

        LogicResponse<List<OfficeWorker>> response = logic.getChiropractors(officeId);

        if (response.getBody() == null) {
            return ErrorResponse.of(response).asResponseEntity();
        }

        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping("/{id}/assistants")
    public ResponseEntity getOfficeAssistants(@PathVariable("id") Integer officeId) {

        //ControllerUtil.checkUserOfficeAccess(user, officeId);

        LogicResponse<List<OfficeWorker>> response = logic.getAssistants(officeId);

        if (response.getBody() == null) {
            return ErrorResponse.of(response).asResponseEntity();
        }

        return ResponseEntity.ok(response.getBody());
    }

    @PostMapping("/{id}/statistics")
    public ResponseEntity queryStatistics(
            @PathVariable("id") Integer officeId, @RequestBody StatisticsQueryRequest request
    ) {

        //ControllerUtil.checkUserOfficeAccess(user, officeId);

        LogicResponse<List<Statistic>> response = logic.getStatistics(officeId, request.getCategory());
        if (response.getBody() == null) {
            return ErrorResponse.of(response).asResponseEntity();
        }

        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping("/{id}/statistics/{statistic_id}")
    public ResponseEntity getStatistic(
            @PathVariable("id") Integer officeId, @PathVariable("statistic_id") Integer statisticId
    ) {
        //ControllerUtil.checkUserOfficeAccess(user, officeId);

        LogicResponse<Statistic> response = logic.getStatistic(officeId, statisticId);
        if (response.getBody() == null) {
            return ErrorResponse.of(response).asResponseEntity();
        }

        return ResponseEntity.ok(response.getBody());
    }

    @PostMapping("/{id}/statistics/{statistic_id}")
    public ResponseEntity modifyStatistic(
            @PathVariable("id") Integer officeId, @PathVariable("statistic_id") Integer statisticId,
            @RequestBody StatisticModifyRequest request
    ) {

        //ControllerUtil.checkUserOfficeAccess(user, officeId);

        LogicResponse<Statistic> response = logic.getStatistic(officeId, statisticId);
        if (response.getBody() == null) {
            return ErrorResponse.of(response).asResponseEntity();
        }

        Statistic stat = response.getBody();

        logic.modifyStatistic(stat, request);

        return ResponseEntity.ok(stat);

    }


}
