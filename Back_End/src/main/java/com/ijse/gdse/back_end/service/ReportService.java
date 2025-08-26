package com.ijse.gdse.back_end.service;

import com.ijse.gdse.back_end.dto.ReportDTO;
import com.ijse.gdse.back_end.entity.Report;
import com.ijse.gdse.back_end.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    public Report addReport(ReportDTO reportDTO) {
        Report report = new Report();
        report.setType(reportDTO.getType());
        report.setLocation(reportDTO.getLocation());
        report.setDescription(reportDTO.getDescription());
        report.setReporterContact(reportDTO.getReporterContact());
        report.setDate(LocalDate.now());

        return reportRepository.save(report);
    }

    public long getTotalReports() {
        return reportRepository.count();
    }

    public Map<String, Long> getMonthlyReports() {
        return reportRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        r -> r.getDate().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                        Collectors.counting()
                ));
    }
}
