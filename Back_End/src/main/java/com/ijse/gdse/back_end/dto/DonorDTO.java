package com.ijse.gdse.back_end.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DonorDTO {

    private String name;
    private String email;
    private BigDecimal amount;
}
