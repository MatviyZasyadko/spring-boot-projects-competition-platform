package com.ukma.competition.platform.users;

import com.ukma.competition.platform.shared.GenericService;
import com.ukma.competition.platform.users.dto.UserRequestDto;
import com.ukma.competition.platform.users.dto.UserResponseDto;
import com.ukma.competition.platform.users.dto.UserUpdateDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public interface UserService extends GenericService<UserEntity, String> {

    Optional<UserEntity> findByEmail(String email);
}
