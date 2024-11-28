package com.ukma.competition.platform.users;

import com.ukma.competition.platform.shared.GenericService;

import java.util.Optional;

public interface UserService extends GenericService<UserEntity, String> {

    Optional<UserEntity> findByEmail(String email);
}
