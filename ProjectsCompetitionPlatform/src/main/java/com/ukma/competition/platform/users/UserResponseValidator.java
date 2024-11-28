package com.ukma.competition.platform.users;

import com.ukma.competition.platform.users.dto.UserResponseDto;

import java.util.Arrays;
import java.util.regex.Pattern;

public class UserResponseValidator {

    public boolean validate(UserResponseDto userResponseDto) {
        boolean userRoleIsSpecifiedCorrectly = Arrays.stream(UserRole.values())
            .map(UserRole::getAuthority)
            .anyMatch(role -> userResponseDto.getRole().toUpperCase().equals(role));
        if (!userRoleIsSpecifiedCorrectly) {
            return false;
        }

        Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        if (!emailPattern.matcher(userResponseDto.getEmail()).matches()) {
            return false;
        }

        return userResponseDto.getId() != null;
    }
}
