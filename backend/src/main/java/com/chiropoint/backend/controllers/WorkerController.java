package com.chiropoint.backend.controllers;

import com.chiropoint.backend.controllers.handlers.ErrorResponse;
import com.chiropoint.backend.domain.models.OfficeWorker;
import com.chiropoint.backend.domain.repositories.OfficeWorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workers")
public class WorkerController {

    @Autowired
    private OfficeWorkerRepository workerRepository;

    @GetMapping("/{id}")
    public ResponseEntity getWorker(@PathVariable("id") Integer id) {
        OfficeWorker worker = id == null ? null : workerRepository.findById(id).orElse(null);
        if (worker == null) {
            return new ErrorResponse(HttpStatus.NOT_FOUND, 40400, "Not found.").asResponseEntity();
        }

        return ResponseEntity.ok(worker);
    }

}
