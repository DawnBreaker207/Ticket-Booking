package com.dawn.backend.service;

import com.dawn.backend.dto.request.TheaterRequestDTO;
import com.dawn.backend.dto.response.TheaterResponseDTO;
import com.dawn.backend.exception.wrapper.TheaterNotFoundException;
import com.dawn.backend.helper.TheaterMappingHelper;
import com.dawn.backend.model.Theater;
import com.dawn.backend.repository.TheaterRepository;
import com.dawn.backend.service.Impl.TheaterServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TheaterService Unit Tests")
public class TheaterServiceTests {
    @Mock
    private TheaterRepository theaterRepository;

    @InjectMocks
    private TheaterServiceImpl theaterService;

    private MockedStatic<TheaterMappingHelper> mappingHelperMock;

    private Theater theater;

    private TheaterRequestDTO theaterRequestDTO;

    private TheaterResponseDTO theaterResponseDTO;

    @BeforeEach
    void setUp() {
        theater = Theater.builder()
                .id(1L)
                .name("Grand Cinema")
                .location("HaNoi")
                .capacity(50)
                .build();

        theaterRequestDTO = TheaterRequestDTO
                .builder()
                .name("Grand Cinema")
                .location("HaNoi")
                .capacity(50)
                .build();

        theaterResponseDTO = TheaterResponseDTO
                .builder()
                .name("Grand Cinema")
                .location("HaNoi")
                .capacity(50)
                .build();
        mappingHelperMock = Mockito.mockStatic(TheaterMappingHelper.class);
    }

    @AfterEach
    void tearDown() {
        mappingHelperMock.close();
    }

    @Test
    void findAll_GivenTheaterExist_WhenCalled_ThenReturnTheaterList() {
        // Arrange
        when(theaterRepository
                .findAll())
                .thenReturn(List.of(theater));

        // Act
        List<TheaterResponseDTO> result = theaterService
                .findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());

        verify(theaterRepository, times(1))
                .findAll();
    }

    @Test
    void findAll_GivenNoTheaterExist_WhenCalled_ThenReturnEmptyList() {
        // Arrange
        when(theaterRepository
                .findAll())
                .thenReturn(Collections.emptyList());

        // Act
        List<TheaterResponseDTO> result = theaterService
                .findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(theaterRepository, times(1))
                .findAll();
    }

    @Test
    void findOne_GivenValidId_WhenCalled_ThenReturnTheaterDto() {
        // Arrange
        when(theaterRepository
                .findById(1L))
                .thenReturn(Optional.of(theater));

        // Act
        TheaterResponseDTO result = theaterService
                .findOne(1L);

        // Assert
        assertNotNull(result);
        assertEquals(theaterResponseDTO.getName(), result.getName());
        verify(theaterRepository, times(1))
                .findById(1L);
    }

    @Test
    void findOne_GivenNullId_WhenNotFound_ThenThrowTheaterNotFoundException() {
        // Arrange
        when(theaterRepository
                .findById(null))
                .thenReturn(Optional.of(theater));

        // Act & Assert
        TheaterNotFoundException exception = assertThrows(
                TheaterNotFoundException.class,
                () -> theaterService.findOne(null));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(theaterRepository, times(1)).findById(null);
    }

    @Test
    void findOne_GivenInvalidId_WhenNotFound_ThenThrowTheaterNotFoundException() {
        // Arrange
        when(theaterRepository
                .findById(999L))
                .thenReturn(Optional.of(theater));

        // Act & Assert
        TheaterNotFoundException exception = assertThrows(
                TheaterNotFoundException.class,
                () -> theaterService.findOne(999L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(theaterRepository, times(1))
                .findById(999L);
    }

    @Test
    void findByLocation_GivenLocation_WhenCalled_ThenReturnTheaterDto() {
        // Arrange
        when(theaterRepository
                .findByLocationContainingIgnoreCase("HaNoi"))
                .thenReturn(List.of(theater));

        // Act
        List<TheaterResponseDTO> result = theaterService.findByLocation("HaNoi");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(theaterResponseDTO.getName(), result.get(0).getName());
        verify(theaterRepository, times(1)).findByLocationContainingIgnoreCase("HaNoi");
    }

    @Test
    void findByLocation_GivenInvalidLocation_WhenNotFound_ThenThrowTheaterNotFoundException() {
        // Arrange
        when(theaterRepository
                .findByLocationContainingIgnoreCase("Unknown"))
                .thenReturn(List.of(theater));

        // Act
        List<TheaterResponseDTO> result = theaterService
                .findByLocation("Unknown");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(theaterRepository, times(1))
                .findByLocationContainingIgnoreCase("Unknown");
    }

    @Test
    void create_WhenSuccess_THenReturnTheater() {
        // Arrange
        when(theaterRepository
                .save(any(Theater.class)))
                .thenReturn(theater);

        // Act
        TheaterResponseDTO result = theaterService
                .create(theaterRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(theaterResponseDTO.getName(), result.getName());
        verify(theaterRepository, times(1))
                .save(any(Theater.class));
    }

    @Test
    void update_GivenValidId_WhenCalled_ThenReturnUpdatedTheater() {
        // Arrange
        when(theaterRepository
                .findById(1L))
                .thenReturn(Optional.of(theater));

        // Act
        TheaterResponseDTO result = theaterService
                .update(1L, theaterRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(theaterResponseDTO.getName(), result.getName());
        verify(theaterRepository, times(1))
                .findById(1L);
        verify(theaterRepository, times(1))
                .save(theater);
    }

    @Test
    void update_GivenInvalidId_WhenNotFound_ThenThrowTheaterNotFoundException() {
        // Arrange
        when(theaterRepository
                .findById(999L))
                .thenReturn(Optional.empty());

        // Act & Assert
        TheaterNotFoundException exception = assertThrows(
                TheaterNotFoundException.class,
                () -> theaterService.update(999L, theaterRequestDTO));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(theaterRepository, times(1))
                .findById(999L);
        verify(theaterRepository, never())
                .save(any());
    }

    @Test
    void remove_GivenValidId_WhenCalled_ThenDeleteTheater() {
        // Arrange
        when(theaterRepository
                .findById(1L))
                .thenReturn(Optional.of(theater));

        // Act
        theaterService
                .remove(1L);

        // Assert
        verify(theaterRepository, times(1))
                .findById(1L);
        verify(theaterRepository, times(1))
                .deleteById(1L);
    }

    @Test
    void remove_GivenInvalidId_WhenNotFound_ThenThrowTheaterNotFoundException() {
        // Arrange
        when(theaterRepository
                .findById(999L))
                .thenReturn(Optional.empty());

        // Act & Assert
        TheaterNotFoundException exception = assertThrows(
                TheaterNotFoundException.class,
                () -> theaterService.remove(999L));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(theaterRepository, times(1))
                .findById(999L);
        verify(theaterRepository, never())
                .deleteById(any());
    }
}
