package com.chiropoint.backend.logic;

import com.chiropoint.backend.domain.models.Office;
import com.chiropoint.backend.domain.models.OfficeWorker;
import com.chiropoint.backend.domain.models.Statistic;
import com.chiropoint.backend.domain.repositories.*;
import com.chiropoint.backend.dto.request.office.StatisticModifyRequest;
import com.chiropoint.backend.dto.response.LogicResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class OfficeLogic {

    @Autowired
    private OfficeRepository officeRepository;
    @Autowired
    private OfficeWorkerRepository workerRepository;

    @Autowired
    private SingleValueStatisticRepository singleValueStatRepository;
    @Autowired
    private PlottableStatisticRepository plottableStatRepository;
    @Autowired
    private StatisticRepositorySuperImpl statisticsRepository;

    public LogicResponse<Office> getOffice(Integer officeId) {
        if (officeId == null) {
            return new LogicResponse<>(40401, "No office ID provided");
        }

        Office office = officeRepository.findById(officeId).orElse(null);

        if (office == null) {
            return new LogicResponse<>(40402, "Invalid office ID");
        }

        return new LogicResponse<>(office);
    }

    public LogicResponse<List<OfficeWorker>> getChiropractors(Integer officeId) {
        if (officeId == null) {
            return new LogicResponse<>(40401, "No office ID provided");
        }

        List<OfficeWorker> chiropractors = workerRepository.findAllChiropractorsByOffice(officeId);

        return new LogicResponse<>(chiropractors);
    }

    public LogicResponse<List<OfficeWorker>> getAssistants(Integer officeId) {
        if (officeId == null) {
            return new LogicResponse<>(40401, "No office ID provided");
        }

        List<OfficeWorker> assistants = workerRepository.findAllAssistantsByOffice(officeId);

        return new LogicResponse<>(assistants);
    }

    public LogicResponse<List<Statistic>> getStatistics(Integer officeId, String category) {
        if (officeId == null) {
            return new LogicResponse<>(40401, "No office ID provided");
        }

        if (category == null || category.isEmpty()) {
            return new LogicResponse<>(40401, "No category provided");
        }

        List<Statistic> statistics;
        if (category.equalsIgnoreCase("ALL")) {
            statistics = statisticsRepository.findAllByOfficeId(officeId);
        } else {
            statistics = statisticsRepository.findAllByOfficeIdAndCategory(officeId, category);
        }

        return new LogicResponse<>(statistics);
    }

    public LogicResponse<Statistic> getStatistic(Integer officeId, Integer statisticId) {
        if (officeId == null) {
            return new LogicResponse<>(40401, "No office ID provided");
        }

        if (statisticId == null) {
            return new LogicResponse<>(40401, "No statistic ID provided");
        }

        Statistic statistic = statisticsRepository.findById(statisticId).orElse(null);

        if (statistic == null) {
            return new LogicResponse<>(40402, "Invalid statistic ID");
        }

        if (statistic.getOffice() == null || !statistic.getOffice().getId().equals(officeId)) {
            return new LogicResponse<>(40001, "That statistic does not belong to the given office.");
        }

        return new LogicResponse<>(statistic);
    }

    public LogicResponse<Statistic> modifyStatistic(Statistic statistic, StatisticModifyRequest request) {
        return new LogicResponse<>(statistic);
    }
}
