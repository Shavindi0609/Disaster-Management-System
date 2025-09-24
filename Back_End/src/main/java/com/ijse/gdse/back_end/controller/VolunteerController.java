package com.ijse.gdse.back_end.controller;


import com.ijse.gdse.back_end.dto.APIResponse;
import com.ijse.gdse.back_end.dto.VolunteerDTO;
import com.ijse.gdse.back_end.entity.Volunteer;
import com.ijse.gdse.back_end.service.VolunteerService;
import com.ijse.gdse.back_end.service.impl.VolunteerServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("/api/volunteers")
@RequiredArgsConstructor

public class VolunteerController {

    private final VolunteerService volunteerService;



    @PostMapping
    public ResponseEntity<APIResponse> addVolunteer(
            @RequestBody @Valid VolunteerDTO volunteerDTO) {
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

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getVolunteerById(@PathVariable Long id) {
        try {
            Volunteer volunteer = volunteerService.getVolunteerById(id); // method returns Volunteer
            return ResponseEntity.ok(
                    new APIResponse(
                            200,
                            "Volunteer fetched successfully",
                            volunteer
                    )
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new APIResponse(
                            404,
                            e.getMessage(),
                            null
                    ));
        }
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

//    // ✅ Toggle Active Status
//    @PatchMapping("/{id}/status")
//    public ResponseEntity<APIResponse> toggleVolunteerStatus(
//            @PathVariable Long id,
//            @RequestBody Map<String, Boolean> statusMap) {
//
//        Boolean active = statusMap.get("active");
//        VolunteerDTO updatedVolunteer = volunteerService.updateVolunteerStatus(id, active);
//
//        return ResponseEntity.ok(
//                new APIResponse(
//                        200,
//                        "Volunteer status updated successfully",
//                        updatedVolunteer
//                )
//        );
//    }
//
//    @GetMapping(params = "email")
//    public ResponseEntity<APIResponse> getVolunteerByEmail(@RequestParam String email) {
//        return volunteerService.getVolunteerByEmail(email)
//                .map(volunteer -> ResponseEntity.ok(
//                        new APIResponse(200, "Volunteer fetched successfully", volunteer)
//                ))
//                .orElse(ResponseEntity.status(404).body(
//                        new APIResponse(404, "Volunteer not found", null)
//                ));
//    }

    // Get logged-in volunteer profile
    @GetMapping("/me")
    public ResponseEntity<APIResponse> getLoggedInVolunteer(Authentication authentication) {
        String email = authentication.getName(); // JWT → email
        try {
            Volunteer volunteer = volunteerService.getVolunteerByEmail(email);
            return ResponseEntity.ok(new APIResponse(200, "Volunteer profile loaded", volunteer));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new APIResponse(404, e.getMessage(), null));
        }
    }

}

