package com.ijse.gdse.back_end.service;

import com.ijse.gdse.back_end.dto.ReportResponseDTO;
import com.ijse.gdse.back_end.entity.ReportResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ReportResponseService {

    ReportResponse addResponse(Long reportId, String volunteerEmail, String statusUpdate, MultipartFile photo) throws IOException;

    List<ReportResponseDTO> getResponsesByVolunteer(Long volunteerId);

    List<ReportResponseDTO> getResponsesByReportDTO(Long reportId);

    List<ReportResponse> getResponsesByReport(Long reportId);
}
