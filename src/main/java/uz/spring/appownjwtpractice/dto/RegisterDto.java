package uz.spring.appownjwtpractice.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.*;

@Data
@RequiredArgsConstructor
public class RegisterDto {

    @NotBlank
    @Size(min = 3,max = 50)
    private String firstname;

    @NotBlank
    @Size(min = 3,max = 50)
    private String lastname;

    @NotBlank(message = "{PHONE_NUMBER_SHOULD_NOT_BE_EMPTY}")
    @Pattern(regexp = "^[+][0-9]{9,15}$", message = "{PHONE_NUMBER_PATTERN}")
    private String phoneNumber;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}
