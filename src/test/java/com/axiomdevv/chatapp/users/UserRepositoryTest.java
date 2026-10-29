package com.axiomdevv.chatapp.users;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

class UserRepositoryTest {

    private static final String EMAIL = "testmail@example.com";
    private static final String USERNAME = "axiom";

    @Autowired
    private UserRepository userRepository;

    @Test
    public void save_user(){
        User savedUser = userRepository.save(new User(USERNAME,EMAIL,"hashedpass"));

        assertNotNull(savedUser.getId());
        assertNotNull(savedUser.getCreatedAt());

    }

    @Test
    public void shouldFindByEmail(){
        userRepository.save(new User(USERNAME,EMAIL,"hashedpass"));

        Optional<User> user = userRepository.findByEmail(EMAIL);

        assertTrue(user.isPresent());
        assertEquals(USERNAME , user.get().getUsername());
    }

    @Test
    public void shouldExitsByEmail(){
        userRepository.save(new User(USERNAME,EMAIL,"hashedpass"));

        boolean emailExist = userRepository.existsByEmail(EMAIL);
        assertTrue(emailExist);
    }

    @Test
    public void shouldExistByUsername(){
        userRepository.save(new User(USERNAME,EMAIL,"hashedpass"));

        boolean usernameExist = userRepository.existsByUsername(USERNAME);
        assertTrue(usernameExist);
    }

    // When data doesnt exist or it's false

    @Test
    public void shouldFindByEmail_returnsEmpty_whenEmailUnknown_orFalse(){
        Optional<User> user = userRepository.findByEmail("mail@mail.com");

        assertTrue(user.isEmpty());

    }

    @Test
    public void existsByEmail_returnsFalse_whenEmailUnknown() {
        boolean result = userRepository.existsByEmail("nobody@example.com");
        assertFalse(result);
    }

    @Test
    public void existsByUsername_returnsFalse_whenUsernameUnknown() {
        boolean result = userRepository.existsByUsername("unknown");
        assertFalse(result);
    }

    @Test
    public void save_throws_whenUsernameAlreadyExists() {
        userRepository.saveAndFlush(new User(USERNAME, EMAIL, "hashedpass"));

        assertThrows(Exception.class, () -> {
            userRepository.saveAndFlush(new User(USERNAME, "other@example.com", "hashedpass"));
        });

    }
}