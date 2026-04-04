package com.vasvass.ppmtool.services;

import com.vasvass.ppmtool.domain.Backlog;
import com.vasvass.ppmtool.domain.Project;
import com.vasvass.ppmtool.domain.ProjectTask;
import com.vasvass.ppmtool.exceptions.ProjectNotFoundException;
import com.vasvass.ppmtool.repositories.BacklogRepository;
import com.vasvass.ppmtool.repositories.ProjectRepository;
import com.vasvass.ppmtool.repositories.ProjectTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectTaskServiceTest {

    @Mock
    private BacklogRepository backlogRepository;
    @Mock
    private ProjectTaskRepository projectTaskRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectTaskService projectTaskService;

    private static final String USERNAME = "user@example.com";
    private static final String PROJECT_ID = "PROJ1";

    private Project project;
    private Backlog backlog;
    private ProjectTask projectTask;

    @BeforeEach
    void setUp() {
        backlog = new Backlog();
        backlog.setProjectIdentifier(PROJECT_ID);
        backlog.setPTSequence(0);

        project = new Project();
        project.setProjectIdentifier(PROJECT_ID);
        project.setBacklog(backlog);

        projectTask = new ProjectTask();
        projectTask.setSummary("Test task");
    }

    // --- addProjectTask ---

    @Test
    void addProjectTask_setsDefaultPriorityAndStatus_whenNullOrZero() {
        when(projectService.findProjectByIdentifier(PROJECT_ID, USERNAME)).thenReturn(project);
        when(projectTaskRepository.save(any(ProjectTask.class))).thenAnswer(inv -> inv.getArgument(0));

        ProjectTask result = projectTaskService.addProjectTask(PROJECT_ID, projectTask, USERNAME);

        assertThat(result.getPriority()).isEqualTo(3);
        assertThat(result.getStatus()).isEqualTo("TO_DO");
    }

    @Test
    void addProjectTask_keepsProvidedPriorityAndStatus() {
        projectTask.setPriority(1);
        projectTask.setStatus("IN_PROGRESS");

        when(projectService.findProjectByIdentifier(PROJECT_ID, USERNAME)).thenReturn(project);
        when(projectTaskRepository.save(any(ProjectTask.class))).thenAnswer(inv -> inv.getArgument(0));

        ProjectTask result = projectTaskService.addProjectTask(PROJECT_ID, projectTask, USERNAME);

        assertThat(result.getPriority()).isEqualTo(1);
        assertThat(result.getStatus()).isEqualTo("IN_PROGRESS");
    }

    @Test
    void addProjectTask_generatesProjectSequence() {
        backlog.setPTSequence(2);

        when(projectService.findProjectByIdentifier(PROJECT_ID, USERNAME)).thenReturn(project);
        when(projectTaskRepository.save(any(ProjectTask.class))).thenAnswer(inv -> inv.getArgument(0));

        ProjectTask result = projectTaskService.addProjectTask(PROJECT_ID, projectTask, USERNAME);

        assertThat(result.getProjectSequence()).isEqualTo("PROJ1-3");
        assertThat(backlog.getPTSequence()).isEqualTo(3);
    }

    @Test
    void addProjectTask_setsProjectIdentifierAndBacklog() {
        when(projectService.findProjectByIdentifier(PROJECT_ID, USERNAME)).thenReturn(project);
        when(projectTaskRepository.save(any(ProjectTask.class))).thenAnswer(inv -> inv.getArgument(0));

        ProjectTask result = projectTaskService.addProjectTask(PROJECT_ID, projectTask, USERNAME);

        assertThat(result.getProjectIdentifier()).isEqualTo(PROJECT_ID);
        assertThat(result.getBacklog()).isEqualTo(backlog);
    }

    @Test
    void addProjectTask_projectNotFound_throwsProjectNotFoundException() {
        when(projectService.findProjectByIdentifier(PROJECT_ID, USERNAME))
                .thenThrow(new ProjectNotFoundException("not found"));

        assertThatThrownBy(() -> projectTaskService.addProjectTask(PROJECT_ID, projectTask, USERNAME))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    // --- findBacklogById ---

    @Test
    void findBacklogById_returnsTasksOrderedByPriority() {
        ProjectTask t1 = new ProjectTask();
        ProjectTask t2 = new ProjectTask();
        List<ProjectTask> tasks = Arrays.asList(t1, t2);

        when(projectTaskRepository.findByProjectIdentifierOrderByPriority(PROJECT_ID)).thenReturn(tasks);

        Iterable<ProjectTask> result = projectTaskService.findBacklogById(PROJECT_ID, USERNAME);

        assertThat(result).containsExactly(t1, t2);
        verify(projectService).findProjectByIdentifier(PROJECT_ID, USERNAME);
    }

    // --- findPTByProjectSequence ---

    @Test
    void findPTByProjectSequence_validTask_returnsTask() {
        projectTask.setProjectSequence("PROJ1-1");
        projectTask.setProjectIdentifier(PROJECT_ID);

        when(projectTaskRepository.findByProjectSequence("PROJ1-1")).thenReturn(projectTask);

        ProjectTask result = projectTaskService.findPTByProjectSequence(PROJECT_ID, "PROJ1-1", USERNAME);

        assertThat(result).isEqualTo(projectTask);
        verify(projectService).findProjectByIdentifier(PROJECT_ID, USERNAME);
    }

    @Test
    void findPTByProjectSequence_taskNotFound_throwsProjectNotFoundException() {
        when(projectTaskRepository.findByProjectSequence("PROJ1-99")).thenReturn(null);

        assertThatThrownBy(() -> projectTaskService.findPTByProjectSequence(PROJECT_ID, "PROJ1-99", USERNAME))
                .isInstanceOf(ProjectNotFoundException.class)
                .hasMessageContaining("PROJ1-99");
    }

    @Test
    void findPTByProjectSequence_taskBelongsToOtherProject_throwsProjectNotFoundException() {
        projectTask.setProjectSequence("PROJ1-1");
        projectTask.setProjectIdentifier("OTHER");

        when(projectTaskRepository.findByProjectSequence("PROJ1-1")).thenReturn(projectTask);

        assertThatThrownBy(() -> projectTaskService.findPTByProjectSequence(PROJECT_ID, "PROJ1-1", USERNAME))
                .isInstanceOf(ProjectNotFoundException.class)
                .hasMessageContaining("PROJ1-1");
    }

    // --- updateByProjectSequence ---

    @Test
    void updateByProjectSequence_updatesAllFields() {
        projectTask.setProjectSequence("PROJ1-1");
        projectTask.setProjectIdentifier(PROJECT_ID);

        ProjectTask update = new ProjectTask();
        update.setSummary("Updated summary");
        update.setAcceptanceCriteria("AC");
        update.setStatus("DONE");
        update.setPriority(1);

        when(projectTaskRepository.findByProjectSequence("PROJ1-1")).thenReturn(projectTask);
        when(projectTaskRepository.save(any(ProjectTask.class))).thenAnswer(inv -> inv.getArgument(0));

        ProjectTask result = projectTaskService.updateByProjectSequence(update, PROJECT_ID, "PROJ1-1", USERNAME);

        assertThat(result.getSummary()).isEqualTo("Updated summary");
        assertThat(result.getAcceptanceCriteria()).isEqualTo("AC");
        assertThat(result.getStatus()).isEqualTo("DONE");
        assertThat(result.getPriority()).isEqualTo(1);
    }

    // --- deletePTByProjectSequence ---

    @Test
    void deletePTByProjectSequence_deletesTask() {
        projectTask.setProjectSequence("PROJ1-1");
        projectTask.setProjectIdentifier(PROJECT_ID);

        when(projectTaskRepository.findByProjectSequence("PROJ1-1")).thenReturn(projectTask);

        projectTaskService.deletePTByProjectSequence(PROJECT_ID, "PROJ1-1", USERNAME);

        verify(projectTaskRepository).delete(projectTask);
    }
}
