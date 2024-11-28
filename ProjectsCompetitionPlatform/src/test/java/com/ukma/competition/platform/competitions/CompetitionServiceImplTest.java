package com.ukma.competition.platform.competitions;

import com.ukma.competition.platform.competitions.business_layer.CompetitionCreateDto;
import com.ukma.competition.platform.competitions.business_layer.CompetitionServiceImpl;
import com.ukma.competition.platform.competitions.database_layer.CompetitionEntity;
import com.ukma.competition.platform.competitions.database_layer.CompetitionRepository;
import com.ukma.competition.platform.users.UserEntity;
import com.ukma.competition.platform.users.UserService;
import com.ukma.edu.spring.boot.starter.cloudinary.service.CloudinaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CompetitionServiceImplTest {

    private CompetitionRepository competitionRepository;
    private CompetitionServiceImpl competitionService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        competitionRepository = Mockito.mock(CompetitionRepository.class);
        userService = Mockito.mock(UserService.class);
        CloudinaryService cloudinaryService = Mockito.mock(CloudinaryService.class);
        competitionService = new CompetitionServiceImpl(
            competitionRepository,
            null,
            null, userService, cloudinaryService, null
        );
    }

    @Test
    void testSaveFromDto_UserNotFound() {
        CompetitionCreateDto createDto = new CompetitionCreateDto();
        createDto.setName("Test Competition");
        createDto.setDescription("Description");
        createDto.setEndDate(LocalDateTime.now());

        when(userService.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () ->
            competitionService.saveFromDto(createDto, "test@example.com")
        );
    }


    @Test
    void testDeleteById_Success() {
        String id = "123";
        when(competitionRepository.existsById(id)).thenReturn(true);

        competitionService.deleteById(id);

        verify(competitionRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteById_NotFound() {
        String id = "123";
        when(competitionRepository.existsById(id)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () ->
            competitionService.deleteById(id)
        );
    }

    @Test
    void testFindByIdAsDto_Success() {
        CompetitionEntity entity = new CompetitionEntity();
        entity.setId("123");
        when(competitionRepository.findById("123")).thenReturn(Optional.of(entity));

        var result = competitionService.findByIdAsDto("123");

        assertNotNull(result);
        assertEquals("123", result.getId());
    }

    @Test
    void testFindAllByOrganizer_ReturnsCompetitions() {
        UserEntity organizer = new UserEntity();
        organizer.setId("123");
        CompetitionEntity competition = new CompetitionEntity();
        competition.setOrganizer(organizer);
        competition.setProjects(List.of());

        when(competitionRepository.findAllByOrganizer(organizer)).thenReturn(List.of(competition));

        var results = competitionService.findAllByOrganizer(organizer);

        assertEquals(1, results.size());
        verify(competitionRepository, times(1)).findAllByOrganizer(organizer);
    }
}
