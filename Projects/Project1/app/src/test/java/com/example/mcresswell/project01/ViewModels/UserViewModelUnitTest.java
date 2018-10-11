package com.example.mcresswell.project01.ViewModels;

import com.example.mcresswell.project01.db.entity.User;
import com.example.mcresswell.project01.db.repo.UserRepository;

import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.Date;

import javax.inject.Inject;

public class UserViewModelUnitTest {

    @Inject
    UserRepository userRepository;

    User user;

    @Inject
    @Before
    public void setUp() throws Exception {
        user = new User();
        user.setFirstName("TESTFIRST");
        user.setLastName("TESTLAST");
        user.setEmail("TEST@TEST.COM");
        user.setPassword("PASSWORD");
        user.setJoinDate(Date.from(Instant.now()));
    }

    @Test
    public void authenticateUser() {
    }

    @Test
    public void createUser() {
        userRepository.insert(user);
    }

    @Test
    public void updateUser() {
    }

    @Test
    public void retrieveUser() {
    }

    @Test
    public void deleteUser() {
    }
}