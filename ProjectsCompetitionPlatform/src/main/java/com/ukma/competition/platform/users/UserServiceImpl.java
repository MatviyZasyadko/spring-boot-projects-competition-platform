package com.ukma.competition.platform.users;

import com.ukma.competition.platform.shared.GenericServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
