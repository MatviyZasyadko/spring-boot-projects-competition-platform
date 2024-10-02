package com.ukma.competition.platform.users;

import com.ukma.competition.platform.shared.GenericService;
import com.ukma.competition.platform.users.dto.UserRequestDto;
import com.ukma.competition.platform.users.dto.UserResponseDto;
import com.ukma.competition.platform.users.dto.UserUpdateDto;

import java.util.List;

public interface UserService extends GenericService<UserEntity, String> {
    UserResponseDto createUser(UserRequestDto userRequestDto);

    UserResponseDto findUserById(String id);

    List<UserResponseDto> findAllUsers();

    UserResponseDto updateUser(String id, UserUpdateDto userUpdateDto);

    boolean deleteUser(String id);
}
