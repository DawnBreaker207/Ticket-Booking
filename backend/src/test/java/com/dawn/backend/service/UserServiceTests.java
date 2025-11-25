package com.dawn.backend.service;

import com.dawn.backend.dto.response.UserResponseDTO;
import com.dawn.backend.exception.wrapper.UserNotFoundException;
import com.dawn.backend.model.User;
import com.dawn.backend.repository.UserRepository;
import com.dawn.backend.service.Impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Unit Tests")
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User
                .builder()
                .id(1L)
                .username("testuser")
                .email("test@gmail.com")
                .build();
    }

    @Test
    public void findAll_GivenUserExist_WhenCalled_ThenReturnsUserList() {
        // Arrange
        when(userRepository
                .findAll(Pageable.unpaged()))
                .thenReturn(new PageImpl<>(List.of(user)));

        // Act
        List<UserResponseDTO> result = userService
                .findAll(Pageable.unpaged())
                .getContent();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(user.getUsername(), result.get(0).getUsername());
        assertEquals(user.getEmail(), result.get(0).getEmail());
        verify(userRepository, times(1))
                .findAll();
    }

    @Test
    public void findAll_GivenNoUserExist_WhenCalled_ThenReturnsEmptyList() {
        // Arrange
        when(userRepository
                .findAll())
                .thenReturn(List.of());
        List<UserResponseDTO> result = userService
                .findAll(Pageable.unpaged())
                .getContent();

        // Act & Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository, times(1))
                .findAll();
    }

    @Test
    public void findOne_GivenValidId_WhenFound_ThenReturnsUserDto() {
        // Arrange
        when(userRepository
                .findById(1L))
                .thenReturn(Optional.of(user));

        // Act
        UserResponseDTO result = userService
                .findOne(1L);

        // Assert
        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getEmail(), result.getEmail());
        verify(userRepository, times(1))
                .findById(1L);
    }

    @Test
    public void findOne_GivenNullId_WhenNotFound_ThenThrowUserNotFoundException() {
        // Act & Assert
        assertThrows(
                Exception.class,
                () -> userService.findOne(null));
    }

    @Test
    public void findOne_GivenInvalidId_WhenNotFound_ThenThrowUserNotFoundException() {
        // Arrange
        when(userRepository
                .findById(1L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                UserNotFoundException.class,
                () -> userService.findOne(1L));
        verify(userRepository, times(1))
                .findById(1L);
    }

    @Test
    public void findByEmail_GivenValidEmail_WhenNotFound_ThenReturnUserDto() {
        // Arrange
        when(userRepository
                .findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        // Act
        UserResponseDTO result = userService
                .findByEmail("test@gmail.com");

        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("test@gmail.com", result.getEmail());
        verify(userRepository, times(1))
                .findByEmail("test@gmail.com");
    }

    @Test
    public void findByEmail_GivenInvalidEmail_WhenNotFound_ThenThrowUserNotFoundException() {
        // Arrange
        when(userRepository
                .findByEmail("test@gmail.com"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                UserNotFoundException.class,
                () -> userService.findByEmail("test@gmail.com"));
        verify(userRepository, times(1))
                .findByEmail("test@gmail.com");
    }

    @Test
    public void updateUser_GivenValidIdAndDetails_WhenUpdated_ThenReturnUpdatedUserDto() {
        //  Arrange
        User updateDetails = User
                .builder()
                .username("updatedUser")
                .email("updated@gmail.com")
                .build();

        when(userRepository
                .findById(1L))
                .thenReturn(Optional.of(user));
        when(userRepository
                .save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        //  Act
        UserResponseDTO result = userService
                .update(1L, updateDetails);

        //  Assert
        assertNotNull(result);
        assertEquals("updatedUser", result.getUsername());
        assertEquals("updated@gmail.com", result.getEmail());
        verify(userRepository, times(1))
                .findById(1L);
        verify(userRepository, times(1))
                .save(any(User.class));
    }

    @Test
    public void updateUser_GivenInvalidId_WhenNotFound_ThenThrowUserNotFoundException() {
        //  Arrange
        User updatedDetails = User
                .builder()
                .username("updatedUser")
                .email("updated@gmail.com")
                .build();

        when(userRepository
                .findById(1L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                UserNotFoundException.class,
                () -> userService.update(1L, updatedDetails));
        verify(userRepository, times(1))
                .findById(1L);
        verify(userRepository, never())
                .save(any());
    }
}
