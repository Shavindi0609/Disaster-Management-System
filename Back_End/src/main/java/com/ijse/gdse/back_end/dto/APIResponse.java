package com.ijse.gdse.back_end.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class APIResponse {
    private int status;
    private String message;
    private Object data;
}
