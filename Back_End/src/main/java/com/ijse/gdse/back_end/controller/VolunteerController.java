package com.ijse.gdse.back_end.controller;


import com.ijse.gdse.back_end.dto.APIResponse;
import com.ijse.gdse.back_end.dto.RegisterDTO;
import com.ijse.gdse.back_end.dto.VolunteerDTO;
import com.ijse.gdse.back_end.entity.Volunteer;
import com.ijse.gdse.back_end.service.VolunteerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("/auth/volunteers")
@RequiredArgsConstructor

public class VolunteerController {

    private final VolunteerService volunteerService;

    @PostMapping
    public ResponseEntity<APIResponse> addVolunteer(
            @RequestBody VolunteerDTO volunteerDTO) {
        return ResponseEntity.ok(
                new APIResponse(
                        200,
                        "Volunteer Added Successfully",
                        volunteerService.addVolunteer(volunteerDTO)
                )
        );
    }

    // Get All Volunteers
    @GetMapping
    public ResponseEntity<APIResponse> getAllVolunteers() {
        return ResponseEntity.ok(
                new APIResponse(
                        200,
                        "Volunteers fetched successfully",
                        volunteerService.getAllVolunteers()
                )
        );
    }

    // Get Volunteer by ID
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getVolunteerById(@PathVariable Long id) {
        return volunteerService.getVolunteerById(id)
                .map(volunteer -> ResponseEntity.ok(
                        new APIResponse(
                                200,
                                "Volunteer fetched successfully",
                                volunteer
                        )
                ))
                .orElse(ResponseEntity.status(404).body(
                        new APIResponse(
                                404,
                                "Volunteer not found",
                                null
                        )
                ));
    }

    // ✅ Update Volunteer
    @PutMapping("/{id}")
    public ResponseEntity<APIResponse> updateVolunteer(
            @PathVariable Long id,
            @RequestBody VolunteerDTO volunteerDTO) {
        return ResponseEntity.ok(
                new APIResponse(
                        200,
                        "Volunteer updated successfully",
                        volunteerService.updateVolunteer(id, volunteerDTO)
                )
        );
    }

    // ✅ Delete Volunteer
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse> deleteVolunteer(@PathVariable Long id) {
        volunteerService.deleteVolunteer(id);
        return ResponseEntity.ok(
                new APIResponse(
                        200,
                        "Volunteer deleted successfully",
                        null
                )
        );
    }

    @GetMapping("/count")
    public ResponseEntity<APIResponse> getVolunteerCount() {
        return ResponseEntity.ok(
                new APIResponse(
                        200,
                        "Volunteer count fetched successfully",
                        volunteerService.countVolunteers()
                )
        );
    }

    // ✅ Toggle Active Status
    @PatchMapping("/{id}/status")
    public ResponseEntity<APIResponse> toggleVolunteerStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> statusMap) {

        Boolean active = statusMap.get("active");
        VolunteerDTO updatedVolunteer = volunteerService.updateVolunteerStatus(id, active);

        return ResponseEntity.ok(
                new APIResponse(
                        200,
                        "Volunteer status updated successfully",
                        updatedVolunteer
                )
        );
    }

    @GetMapping(params = "email")
    public ResponseEntity<APIResponse> getVolunteerByEmail(@RequestParam String email) {
        return volunteerService.getVolunteerByEmail(email)
                .map(volunteer -> ResponseEntity.ok(
                        new APIResponse(200, "Volunteer fetched successfully", volunteer)
                ))
                .orElse(ResponseEntity.status(404).body(
                        new APIResponse(404, "Volunteer not found", null)
                ));
    }


}

