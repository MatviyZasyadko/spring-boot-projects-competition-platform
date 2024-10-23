package com.ukma.competition.platform.users;

import com.ukma.competition.platform.shared.GenericServiceImpl;
import com.ukma.competition.platform.users.dto.UserRequestDto;
import com.ukma.competition.platform.users.dto.UserResponseDto;
import com.ukma.competition.platform.users.dto.UserUpdateDto;
import org.apache.logging.log4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends GenericServiceImpl<UserEntity, String, UserRepository> implements UserService {

    private static UserRepository userRepository;
    private static final Marker USER_MARKER = MarkerManager.getMarker("USER");

    @Value("${user.password.min-length}")
    private int minPasswordLength;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        super(repository);
        userRepository = repository;
    }

    public UserResponseDto createUser(UserRequestDto userRequestDto) {

        ThreadContext.put("email", userRequestDto.getEmail());

        if (userRequestDto.getPassword().length() < minPasswordLength) {
            throw new IllegalArgumentException("Password must be at least " + minPasswordLength + " characters long.");
        }
        UserEntity newUser = UserEntity.builder()
                .email(userRequestDto.getEmail())
                .fullName(userRequestDto.getFullName())
                .password(userRequestDto.getPassword())
                .build();

        UserEntity savedUser = userRepository.save(newUser);

        logger.info("User created successfully");

        ThreadContext.clearAll();

        return mapToUserResponseDto(savedUser);
    }

    public UserResponseDto findUserById(String id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + id + " not found."));

        return mapToUserResponseDto(user);
    }

    public List<UserResponseDto> findAllUsers() {
        List<UserResponseDto> users = userRepository.findAll().stream()
                .map(UserServiceImpl::mapToUserResponseDto)
                .collect(Collectors.toList());

        logger.info(USER_MARKER, "Retrieved all users: {}", users.size());
        return users;
    }

    @Transactional
    public UserResponseDto updateUser(String id, UserUpdateDto userUpdateDto) {

        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + id + " not found."));

        existingUser.setFullName(userUpdateDto.getFullName());
        existingUser.setPassword(userUpdateDto.getPassword());

        UserEntity updatedUser = userRepository.save(existingUser);

        logger.info("User updated successfully");

        return mapToUserResponseDto(updatedUser);
    }

    @Transactional
    public boolean deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User with ID " + id + " does not exist.");
        }
        userRepository.deleteById(id);
        return true;
    }

    private static UserResponseDto mapToUserResponseDto(UserEntity user) {
        return UserResponseDto.builder()
                .id(Long.valueOf(user.getId()))
                .email(user.getEmail())
                .fullName(user.getFullName())
                .projectIds(user.getProjects().stream()
                        .map(project -> Long.valueOf(project.getId()))
                        .collect(Collectors.toList()))
                .build();
    }

}
