package com.ukma.competition.platform.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RegistrationRequestDto {

    @NotBlank
    @Length(min = 2, max = 50, message = "Full name should be in range from 2 to 50 symbols!")
    String fullName;

    @Email(message = "Email is not formatted properly!")
    String email;

    @NotBlank
    @Length(min = 5, max = 15, message = "Password should be in range from 6 to 15 symbols!")
    String password;
}
