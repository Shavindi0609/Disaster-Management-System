package com.ijse.gdse.back_end.dto;


import lombok.Data;

@Data
public class VolunteerDTO {

    private String name;
    private String email;
    private String phone;
    private String skills;

    private Boolean active = true; // ✅ Add this to support active/inactive toggle

}
