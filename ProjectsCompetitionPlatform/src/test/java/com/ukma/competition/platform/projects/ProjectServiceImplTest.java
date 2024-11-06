package com.ukma.competition.platform.projects;

import com.ukma.competition.platform.projects.dto.ProjectCreateDto;
import com.ukma.competition.platform.users.UserEntity;
import com.ukma.competition.platform.users.UserService;
import com.ukma.edu.spring.boot.starter.cloudinary.service.CloudinaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserService userService;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Value("${spring.cloudinary.folder}")
    private String cloudinaryFolder;

    private static final String PROJECT_ID = "projectId123";
    private static final String USER_EMAIL = "user@example.com";
    private static final String IMAGE_URL = "http://example.com/logo.jpg";

    private UserEntity projectCreator;

    @BeforeEach
    void setUp() {
        projectService = new ProjectServiceImpl(projectRepository, userService, cloudinaryService);
        projectCreator = UserEntity.builder().email(USER_EMAIL).build();
    }

    @Test
    void saveFromDto_SuccessWithLogo() throws IOException {
        ProjectCreateDto projectCreateDto = new ProjectCreateDto();
        projectCreateDto.setName("Test Project");
        projectCreateDto.setFullDescription("Full Description");
        projectCreateDto.setShortDescription("Short Description");

        MultipartFile logoFile = mock(MultipartFile.class);
        when(logoFile.isEmpty()).thenReturn(false);
        projectCreateDto.setLogo(logoFile);

        when(userService.findByEmail(USER_EMAIL)).thenReturn(Optional.of(projectCreator));
        when(cloudinaryService.upload(logoFile, cloudinaryFolder)).thenReturn(IMAGE_URL);

        projectService.saveFromDto(projectCreateDto, USER_EMAIL);

        ArgumentCaptor<ProjectEntity> projectCaptor = ArgumentCaptor.forClass(ProjectEntity.class);
        verify(projectRepository, times(1)).saveAndFlush(projectCaptor.capture());

        ProjectEntity savedProject = projectCaptor.getValue();

        assertThat(savedProject.getName()).isEqualTo("Test Project");
        assertThat(savedProject.getImages()).hasSize(1);
    }

    @Test
    void saveFromDto_SuccessWithoutLogo() throws IOException {
        ProjectCreateDto projectCreateDto = new ProjectCreateDto();
        projectCreateDto.setName("Test Project");
        projectCreateDto.setFullDescription("Full Description");
        projectCreateDto.setShortDescription("Short Description");

        when(userService.findByEmail(USER_EMAIL)).thenReturn(Optional.of(projectCreator));

        projectService.saveFromDto(projectCreateDto, USER_EMAIL);

        ArgumentCaptor<ProjectEntity> projectCaptor = ArgumentCaptor.forClass(ProjectEntity.class);
        verify(projectRepository, times(1)).saveAndFlush(projectCaptor.capture());

        ProjectEntity savedProject = projectCaptor.getValue();
        assertThat(savedProject.getName()).isEqualTo("Test Project");
        assertThat(savedProject.getImages()).isEmpty();
    }

    @Test
    void saveFromDto_UserNotFound() {
        ProjectCreateDto projectCreateDto = new ProjectCreateDto();
        when(userService.findByEmail(USER_EMAIL)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> projectService.saveFromDto(projectCreateDto, USER_EMAIL))
            .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void findAll_ReturnsProjects() {
        ProjectEntity project = ProjectEntity.builder().name("Test Project").build();
        project.setId(PROJECT_ID);
        when(projectRepository.findAll()).thenReturn(List.of(project));

        List<ProjectEntity> result = projectService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(PROJECT_ID);
    }

    @Test
    void findById_Success() {
        ProjectEntity project = ProjectEntity.builder().name("Test Project").build();
        project.setId(PROJECT_ID);
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));

        Optional<ProjectEntity> result = projectService.findById(PROJECT_ID);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(PROJECT_ID);
    }

    @Test
    void findById_NotFound() {
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        Optional<ProjectEntity> result = projectService.findById(PROJECT_ID);

        assertThat(result).isNotPresent();
    }

    @Test
    void deleteById_Success() {
        when(projectRepository.existsById(PROJECT_ID)).thenReturn(true);

        projectService.deleteById(PROJECT_ID);

        verify(projectRepository, times(1)).deleteById(PROJECT_ID);
    }

    @Test
    void deleteById_NotFound() {
        when(projectRepository.existsById(PROJECT_ID)).thenReturn(false);

        assertThatThrownBy(() -> projectService.deleteById(PROJECT_ID))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessageContaining("Record is not found while trying to delete");

        verify(projectRepository, never()).deleteById(PROJECT_ID);
    }
}
