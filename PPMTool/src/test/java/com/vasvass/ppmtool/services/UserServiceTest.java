package com.vasvass.ppmtool.services;

import com.vasvass.ppmtool.domain.User;
import com.vasvass.ppmtool.exceptions.UsernameAlreadyExistsException;
import com.vasvass.ppmtool.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("test@example.com");
        user.setFullName("Test User");
        user.setPassword("plainPassword");
        user.setConfirmPassword("plainPassword");
    }

    @Test
    void saveUser_encodesPasswordAndClearsConfirmPassword() {
        when(bCryptPasswordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User saved = userService.saveUser(user);

        assertThat(saved.getPassword()).isEqualTo("encodedPassword");
        assertThat(saved.getConfirmPassword()).isEmpty();
        verify(userRepository).save(user);
    }

    @Test
    void saveUser_returnsPersistedUser() {
        User persisted = new User();
        persisted.setId(1L);
        persisted.setUsername("test@example.com");
        when(bCryptPasswordEncoder.encode(any())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(persisted);

        User result = userService.saveUser(user);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUsername()).isEqualTo("test@example.com");
    }

    @Test
    void saveUser_duplicateUsername_throwsUsernameAlreadyExistsException() {
        when(bCryptPasswordEncoder.encode(any())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenThrow(new DataIntegrityViolationException("duplicate"));

        assertThatThrownBy(() -> userService.saveUser(user))
                .isInstanceOf(UsernameAlreadyExistsException.class)
                .hasMessageContaining("test@example.com");
    }
}
