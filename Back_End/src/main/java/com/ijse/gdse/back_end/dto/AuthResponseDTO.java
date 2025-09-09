package com.ijse.gdse.back_end.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class AuthResponseDTO {
    private String accessToken;
    private String username;
    private String role;
    private String email;


}
