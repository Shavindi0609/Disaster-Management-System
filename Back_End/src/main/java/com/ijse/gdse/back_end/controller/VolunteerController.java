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

@CrossOrigin
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

}

