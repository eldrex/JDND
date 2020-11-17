package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.apache.catalina.connector.Response;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObject(userController, "userRepository", userRepository);
        TestUtils.injectObject(userController, "cartRepository", cartRepository);
        TestUtils.injectObject(userController, "bCrypt", encoder);
    }

    @Test
    public void create_user_happy_path() {
        when(encoder.encode("testPwd")).thenReturn("hashed");

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPwd");
        r.setConfirmPassword("testPwd");

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(Response.SC_OK, response.getStatusCodeValue());

        User u = response.getBody();

        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("hashed", u.getPassword());

    }

    @Test
    public void create_user_passwords_does_not_match() {
        when(encoder.encode("testPwd")).thenReturn("hashed");

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPwd");
        r.setConfirmPassword("testPwd1");

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(Response.SC_BAD_REQUEST, response.getStatusCodeValue());
    }

    @Test
    public void create_user_passwords_does_not_match_length_constrain() {
        when(encoder.encode("testPwd")).thenReturn("hashed");

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("short");
        r.setConfirmPassword("short");

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(Response.SC_BAD_REQUEST, response.getStatusCodeValue());
    }

    @Test
    public void find_by_user_name_test() {

        User u = getTestUser();

        when(userRepository.findByUsername(u.getUsername())).thenReturn(u);

        final ResponseEntity<User> response = userController.findByUserName(u.getUsername());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User foundUser = response.getBody();

        assertNotNull(foundUser);
        assertEquals(u.getUsername(), u.getUsername());
    }

    @Test
    public void find_by_id_test() {
        long id = 1L;

        User u = getTestUser();

        when(userRepository.findById(id)).thenReturn(Optional.ofNullable(u));

        final ResponseEntity<User> response = userController.findById(id);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User foundUser = response.getBody();

        assertNotNull(foundUser);
        assertEquals(foundUser.getId(), u.getId());
        assertEquals(foundUser.getUsername(), u.getUsername());
    }

    private User getTestUser() {
        User u = new User();
        u.setId(1);
        u.setUsername("test");
        u.setPassword("optional");

        return u;
    }

}
