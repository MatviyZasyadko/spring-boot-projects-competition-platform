package com.ukma.competition.platform.users;

import com.ukma.competition.platform.shared.GenericServiceImpl;
import com.ukma.competition.platform.users.dto.UserRequestDto;
import com.ukma.competition.platform.users.dto.UserResponseDto;
import com.ukma.competition.platform.users.dto.UserUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl extends GenericServiceImpl<UserEntity, String, UserRepository> implements UserService {

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        super(repository);
    }


    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return this.repository.findByEmail(email);
    }
}
