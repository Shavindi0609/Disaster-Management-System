package com.ijse.gdse.back_end.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class VolunteerDTO {

    @NotBlank(message = "Name is mandatory")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Name must contain only letters and spaces")
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is mandatory")
    @Pattern(
            regexp = "^(\\+\\d{1,3}[- ]?)?\\d{9,15}$",
            message = "Phone number must be valid"
    )
    private String phone;

    @Size(max = 200, message = "Skills description must not exceed 200 characters")
    private String skills;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, max = 100, message = "Password must be at least 6 characters long")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
            message = "Password must contain at least one letter and one number"
    )
    private String password; // Required for signup/login
}
