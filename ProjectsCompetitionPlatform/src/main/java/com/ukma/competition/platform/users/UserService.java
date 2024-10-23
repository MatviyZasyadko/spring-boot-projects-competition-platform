package com.ukma.competition.platform.users;

import com.ukma.competition.platform.shared.GenericService;
import com.ukma.competition.platform.users.dto.UserRequestDto;
import com.ukma.competition.platform.users.dto.UserResponseDto;
import com.ukma.competition.platform.users.dto.UserUpdateDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public interface UserService extends GenericService<UserEntity, String> {

    public Logger logger = LogManager.getLogger(UserServiceImpl.class);

    UserResponseDto createUser(UserRequestDto userRequestDto);

    UserResponseDto findUserById(String id);

    List<UserResponseDto> findAllUsers();

    UserResponseDto updateUser(String id, UserUpdateDto userUpdateDto);

    boolean deleteUser(String id);
}
