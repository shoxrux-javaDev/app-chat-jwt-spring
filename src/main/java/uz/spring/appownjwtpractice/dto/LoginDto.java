package uz.spring.appownjwtpractice.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class LoginDto {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}
