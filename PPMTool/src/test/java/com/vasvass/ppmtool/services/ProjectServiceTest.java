package com.vasvass.ppmtool.services;

import com.vasvass.ppmtool.domain.Backlog;
import com.vasvass.ppmtool.domain.Project;
import com.vasvass.ppmtool.domain.User;
import com.vasvass.ppmtool.exceptions.ProjectIdException;
import com.vasvass.ppmtool.exceptions.ProjectNotFoundException;
import com.vasvass.ppmtool.repositories.BacklogRepository;
import com.vasvass.ppmtool.repositories.ProjectRepository;
import com.vasvass.ppmtool.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private BacklogRepository backlogRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProjectService projectService;

    private User user;
    private Project project;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("owner@example.com");

        project = new Project();
        project.setProjectName("Test Project");
        project.setProjectIdentifier("test1");
        project.setDescription("A test project");
    }

    // --- saveOrUpdateProject (create) ---

    @Test
    void saveOrUpdateProject_newProject_createsBacklogAndSetsLeader() {
        when(userRepository.findByUsername("owner@example.com")).thenReturn(user);
        when(projectRepository.save(any(Project.class))).thenAnswer(inv -> inv.getArgument(0));

        Project result = projectService.saveOrUpdateProject(project, "owner@example.com");

        assertThat(result.getProjectIdentifier()).isEqualTo("TEST1");
        assertThat(result.getProjectLeader()).isEqualTo("owner@example.com");
        assertThat(result.getUser()).isEqualTo(user);
        assertThat(result.getBacklog()).isNotNull();
        assertThat(result.getBacklog().getProjectIdentifier()).isEqualTo("TEST1");
    }

    @Test
    void saveOrUpdateProject_newProject_identifierIsUppercased() {
        when(userRepository.findByUsername(any())).thenReturn(user);
        when(projectRepository.save(any(Project.class))).thenAnswer(inv -> inv.getArgument(0));

        project.setProjectIdentifier("abcd");
        Project result = projectService.saveOrUpdateProject(project, "owner@example.com");

        assertThat(result.getProjectIdentifier()).isEqualTo("ABCD");
    }

    @Test
    void saveOrUpdateProject_duplicateIdentifier_throwsProjectIdException() {
        when(userRepository.findByUsername(any())).thenReturn(user);
        when(projectRepository.save(any(Project.class))).thenThrow(new DataIntegrityViolationException("duplicate"));

        assertThatThrownBy(() -> projectService.saveOrUpdateProject(project, "owner@example.com"))
                .isInstanceOf(ProjectIdException.class)
                .hasMessageContaining("TEST1");
    }

    @Test
    void saveOrUpdateProject_updateProject_ownedByUser_succeeds() {
        project.setId(10L);
        project.setProjectIdentifier("TEST1");

        Project existing = new Project();
        existing.setProjectLeader("owner@example.com");

        Backlog backlog = new Backlog();
        backlog.setProjectIdentifier("TEST1");

        when(projectRepository.findByProjectIdentifier("TEST1")).thenReturn(existing);
        when(backlogRepository.findByProjectIdentifier("TEST1")).thenReturn(backlog);
        when(userRepository.findByUsername("owner@example.com")).thenReturn(user);
        when(projectRepository.save(any(Project.class))).thenAnswer(inv -> inv.getArgument(0));

        Project result = projectService.saveOrUpdateProject(project, "owner@example.com");

        assertThat(result.getBacklog()).isEqualTo(backlog);
    }

    @Test
    void saveOrUpdateProject_updateProject_notOwner_throwsProjectNotFoundException() {
        project.setId(10L);
        project.setProjectIdentifier("TEST1");

        Project existing = new Project();
        existing.setProjectLeader("other@example.com");

        when(projectRepository.findByProjectIdentifier("TEST1")).thenReturn(existing);

        assertThatThrownBy(() -> projectService.saveOrUpdateProject(project, "owner@example.com"))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    // --- findProjectByIdentifier ---

    @Test
    void findProjectByIdentifier_found_andOwner_returnsProject() {
        project.setProjectLeader("owner@example.com");
        when(projectRepository.findByProjectIdentifier("TEST1")).thenReturn(project);

        Project result = projectService.findProjectByIdentifier("test1", "owner@example.com");

        assertThat(result).isEqualTo(project);
    }

    @Test
    void findProjectByIdentifier_notFound_throwsProjectIdException() {
        when(projectRepository.findByProjectIdentifier("NOTFOUND")).thenReturn(null);

        assertThatThrownBy(() -> projectService.findProjectByIdentifier("notfound", "owner@example.com"))
                .isInstanceOf(ProjectIdException.class)
                .hasMessageContaining("notfound");
    }

    @Test
    void findProjectByIdentifier_foundButNotOwner_throwsProjectNotFoundException() {
        project.setProjectLeader("other@example.com");
        when(projectRepository.findByProjectIdentifier("TEST1")).thenReturn(project);

        assertThatThrownBy(() -> projectService.findProjectByIdentifier("test1", "owner@example.com"))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    // --- findAllProjects ---

    @Test
    void findAllProjects_returnsProjectsForUser() {
        List<Project> projects = Arrays.asList(project);
        when(projectRepository.findAllByProjectLeader("owner@example.com")).thenReturn(projects);

        Iterable<Project> result = projectService.findAllProjects("owner@example.com");

        assertThat(result).containsExactly(project);
    }

    // --- deleteProjectByIdentifier ---

    @Test
    void deleteProjectByIdentifier_deletesOwnedProject() {
        project.setProjectLeader("owner@example.com");
        when(projectRepository.findByProjectIdentifier("TEST1")).thenReturn(project);

        projectService.deleteProjectByIdentifier("test1", "owner@example.com");

        verify(projectRepository).delete(project);
    }

    @Test
    void deleteProjectByIdentifier_notOwner_throwsException() {
        project.setProjectLeader("other@example.com");
        when(projectRepository.findByProjectIdentifier("TEST1")).thenReturn(project);

        assertThatThrownBy(() -> projectService.deleteProjectByIdentifier("test1", "owner@example.com"))
                .isInstanceOf(ProjectNotFoundException.class);

        verify(projectRepository, never()).delete(any());
    }
}
