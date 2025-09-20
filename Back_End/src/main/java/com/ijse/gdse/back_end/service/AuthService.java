package com.ijse.gdse.back_end.service;

import com.ijse.gdse.back_end.dto.AuthDTO;
import com.ijse.gdse.back_end.dto.AuthResponseDTO;
import com.ijse.gdse.back_end.dto.RegisterDTO;

public interface AuthService {

    AuthResponseDTO authenticate(AuthDTO authDTO);

    String register(RegisterDTO registerDTO);
}
