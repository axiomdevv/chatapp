package com.axiomdevv.chatapp.users;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(UserService.class)
class UserServiceTest {

    private static final String USERNAME1 = "axiom";
    private static final String USERNAME2 = "john";
    private static final String EMAIL1 = "axiom@gmail.com";
    private static final String EMAIL2 = "john.macaron@gmail.com";
    private static final String PASSWORD = "passwordHash";

    @Autowired
    private UserService userService;


    @Test
    void testCreateUser() {
        User user = userService.createUser(USERNAME1,EMAIL1,PASSWORD);
        assertNotNull(user.getId());
        assertNotNull(user.getCreatedAt());
        assertEquals(USERNAME1, user.getUsername());
        assertEquals(EMAIL1, user.getEmail());

    }

    @Test
    void testUsernameTaken() {
        userService.createUser(USERNAME1,EMAIL1,PASSWORD);
        assertThrows( UsernameTakenException.class , () -> userService.createUser(USERNAME1,EMAIL2,PASSWORD));
    }

    @Test
    void testEmailTaken() {
        userService.createUser(USERNAME1,EMAIL1,PASSWORD);
        assertThrows( EmailTakenException.class , () -> userService.createUser(USERNAME2,EMAIL1,PASSWORD));
    }


    @Test
    void testFindByUsername() {
        userService.createUser(USERNAME1,EMAIL1,PASSWORD);
        assertTrue(userService.findByUsername(USERNAME1).isPresent());

    }

    @Test
    void testFindByEmail() {
        userService.createUser(USERNAME1,EMAIL1,PASSWORD);
        assertTrue(userService.findByEmail(EMAIL1).isPresent());

    }
}