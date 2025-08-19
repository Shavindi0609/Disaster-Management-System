package com.ijse.gdse.back_end.controller;


import com.ijse.gdse.back_end.entity.DisasterReport;
import com.ijse.gdse.back_end.repository.DisasterReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:63342") // Frontend origin
@RestController
@RequestMapping("/api/disasters")
public class DisasterController {

    @Autowired
    private DisasterReportRepository disasterRepository;

    @PostMapping("/report")
    public DisasterReport reportDisaster(@RequestBody DisasterReport report) {
        return disasterRepository.save(report);
    }

    @GetMapping("/all")
    public List<DisasterReport> getAllReports() {
        return disasterRepository.findAll();
    }
}
