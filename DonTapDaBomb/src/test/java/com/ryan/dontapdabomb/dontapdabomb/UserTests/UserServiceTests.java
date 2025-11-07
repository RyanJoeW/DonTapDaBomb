package com.ryan.dontapdabomb.dontapdabomb.UserTests;

import com.ryan.dontapdabomb.dontapdabomb.entity.User;
import com.ryan.dontapdabomb.dontapdabomb.repository.UserRepository;
import com.ryan.dontapdabomb.dontapdabomb.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        // Arrange
        User user1 = new User(1L, "Test1", "testpassword", 100);
        User user2 = new User(1L, "Test2", "testpassword", 100);
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // Act
        List<User> users = userService.getAllUsers();

        // Assert
        assertThat(users).hasSize(2);
        assertThat(users.get(0).getName()).isEqualTo("Test1");
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_ShouldReturnUser_WhenExists() {
        // Arrange
        User user = new User(1L, "Test1", "testpassword", 100);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        User found = userService.getUserById(1L);

        // Assert
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Test1");
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void addUser_ShouldSaveAndReturnUser() {
        // Arrange
        User user = new User(1L, "Test1", "testpassword", 100);
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User saved = userService.addUser(user);

        // Assert
        assertThat(saved).isEqualTo(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUser_ShouldSaveAndReturnUser() {
        // Arrange
        User user = new User(1L, "Updated", "testpassword", 100);
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User updated = userService.updateUser(user);

        // Assert
        assertThat(updated.getName()).isEqualTo("Updated");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void deleteUser_ShouldDeleteAndReturnMessage() {
        // Arrange
        User user = new User(1L, "Updated", "testpassword", 100);

        // Act
        String result = userService.deleteUser(user);

        // Assert
        assertThat(result).isEqualTo("User has been deleted");
        verify(userRepository, times(1)).delete(user);
    }
}