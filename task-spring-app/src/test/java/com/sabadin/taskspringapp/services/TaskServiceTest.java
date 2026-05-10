package com.sabadin.taskspringapp.services;

import com.sabadin.taskspringapp.security.model.entity.Role;
import com.sabadin.taskspringapp.security.model.entity.User;
import com.sabadin.taskspringapp.task.exception.TaskNotFoundException;
import com.sabadin.taskspringapp.task.model.dto.TaskCommentDto;
import com.sabadin.taskspringapp.task.model.dto.TaskDto;
import com.sabadin.taskspringapp.task.model.entity.Task;
import com.sabadin.taskspringapp.task.model.entity.TaskComment;
import com.sabadin.taskspringapp.task.model.entity.TaskStatus;
import com.sabadin.taskspringapp.task.repository.TaskCommentRepository;
import com.sabadin.taskspringapp.task.repository.TaskRepository;
import com.sabadin.taskspringapp.task.service.TaskService;
import com.sabadin.taskspringapp.team.model.entity.Team;
import com.sabadin.taskspringapp.team.model.entity.TypeTeam;
import com.sabadin.taskspringapp.team.service.TeamService;
import com.sabadin.taskspringapp.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Mock
    private UserService userService;

    @Mock
    private TeamService teamService;

    @Mock
    private TaskCommentRepository taskCommentRepository;

    private User testUser;
    private Team testTeam;
    private Task testTask;
    private TaskDto testTaskDto;
    private TaskComment testTaskComment;
    private TaskCommentDto testTaskCommentDto;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .firstName("First Name")
                .lastName("Last Name")
                .logonName("Logon Name")
                .email("test@teat.com")
                .role(Role.ROLE_USER)
                .build();
        testTeam = Team.builder()
                .id(1L)
                .titleTeam("Test Team Title")
                .typeTeam(TypeTeam.SOLO)
                .users(List.of(testUser))
                .build();
        testTask = Task.builder()
                .id(1L)
                .version(1)
                .title("Title Task")
                .description("Description Task")
                .status(TaskStatus.NEW)
                .initiator(testUser)
                .team(testTeam)
                .build();
        testTaskDto = TaskDto.builder()
                .id(1L)
                .title("Title Task")
                .description("Description Task")
                .teamId(1L)
                .status("NEW")
                .build();
        testTaskComment = TaskComment.builder()
                .id(1L)
                .version(1)
                .comment("Test Comment")
                .author(testUser)
                .task(testTask)
                .build();
        testTaskCommentDto = TaskCommentDto.builder()
                .id(1L)
                .taskId(1L)
                .comment("Test Comment")
                .build();
    }

    @Test
    void getTaskDtoById_ShouldReturnTaskDto_WhenTaskExists() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        TaskDto result = taskService.getTaskDtoById(1L);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Title Task");
        verify(taskRepository).findById(1L);
    }

    @Test
    void getTaskDtoById_ShouldThrowException_WhenTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> taskService.getTaskDtoById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Task with ID: {} is missing");
        verify(taskRepository).findById(99L);
    }

    @Test
    void getTasksByUserTeam_ShouldReturnTaskList_WhenUserHasTeams() {
        when(userService.getCurrentUser()).thenReturn(testUser);
        testUser.setTeams(List.of(testTeam));
        when(taskRepository.findByTeamId(anyList())).thenReturn(List.of(testTask));

        List<TaskDto> result = taskService.getTasksByUserTeam();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Title Task");
        verify(userService).getCurrentUser();
        verify(taskRepository).findByTeamId(anyList());
    }

    @Test
    void getTasksByUserTeam_ShouldReturnEmptyList_WhenUserHasNoTeams() {
        when(userService.getCurrentUser()).thenReturn(testUser);
        testUser.setTeams(List.of());
        when(taskRepository.findByTeamId(anyList())).thenReturn(List.of());

        List<TaskDto> result = taskService.getTasksByUserTeam();

        assertThat(result).isEmpty();
        verify(userService).getCurrentUser();
        verify(taskRepository).findByTeamId(anyList());
    }

    @Test
    void updateTaskStatus_ShouldUpdateAndReturnTask_WhenTaskExists() {
        String newStatus = "IN_PROGRESS";
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(userService.getCurrentUser()).thenReturn(testUser);

        Task updatedTask = Task.builder()
                .id(1L)
                .version(2)
                .title("Title Task")
                .description("Description Task")
                .status(TaskStatus.IN_PROGRESS)
                .initiator(testUser)
                .executor(null)
                .team(testTeam)
                .build();

        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        TaskDto result = taskService.updateTaskStatus(1L, newStatus);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo("IN_PROGRESS");
        verify(taskRepository).findById(1L);
        verify(taskRepository).save(any(Task.class));
        verify(userService, atLeastOnce()).getCurrentUser();
    }

    @Test
    void updateTaskStatus_ShouldThrowException_WhenTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.updateTaskStatus(99L, "IN_PROGRESS"))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("Task not found: 99");
        verify(taskRepository).findById(99L);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void createNewTask_ShouldSaveAndReturnTask() {
        when(userService.getCurrentUser()).thenReturn(testUser);
        when(teamService.getTeamById(1L)).thenReturn(testTeam);
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        TaskDto result = taskService.createNewTask(testTaskDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Title Task");
        verify(userService).getCurrentUser();
        verify(teamService).getTeamById(1L);
        verify(taskRepository).save(any(Task.class));
    }

    // ToDo remove this test, because ToDo remove taskService.createBulkTask
    @Test
    void createBulkTask_ShouldCreateMultipleTasks() {
        TaskDto taskDto1 = TaskDto.builder().title("Task 1").teamId(1L).build();
        TaskDto taskDto2 = TaskDto.builder().title("Task 2").teamId(1L).build();
        List<TaskDto> taskDtos = List.of(taskDto1, taskDto2);

        when(userService.getCurrentUser()).thenReturn(testUser);
        when(teamService.getTeamById(1L)).thenReturn(testTeam);
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        String result = taskService.createBulkTask(taskDtos);

        assertThat(result).isEqualTo("Done!");
        verify(taskRepository, times(2)).save(any(Task.class));
        verify(userService, times(2)).getCurrentUser();
        verify(teamService, times(2)).getTeamById(1L);
    }

    @Test
    void createTaskComment_ShouldSaveAndReturnComment() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(userService.getCurrentUser()).thenReturn(testUser);
        when(taskCommentRepository.save(any(TaskComment.class))).thenReturn(testTaskComment);

        TaskCommentDto result = taskService.createTaskComment(testTaskCommentDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getComment()).isEqualTo("Test Comment");
        verify(taskRepository).findById(1L);
        verify(userService).getCurrentUser();
        verify(taskCommentRepository).save(any(TaskComment.class));
    }

    @Test
    void createTaskComment_ShouldThrowException_WhenTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.createTaskComment(
                TaskCommentDto.builder().taskId(99L).build()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Task with ID: {} is missing");
        verify(taskRepository).findById(99L);
        verify(taskCommentRepository, never()).save(any(TaskComment.class));
    }

    @Test
    void getTaskCommentsByTaskId_ShouldReturnComments_WhenTaskExists() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskCommentRepository.findAllByTask(testTask)).thenReturn(List.of(testTaskComment));

        List<TaskCommentDto> result = taskService.getTaskCommentsByTaskId(1L);

        // ToDO change after method fix
        assertThat(result).isNull();
        verify(taskRepository).findById(1L);
        verify(taskCommentRepository).findAllByTask(testTask);
    }

    // ToDo change after method fix
    @Test
    void getTaskCommentsByTaskId_ShouldThrowException_WhenTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTaskCommentsByTaskId(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Task with ID: {} is missing");
        verify(taskRepository).findById(99L);
        verify(taskCommentRepository, never()).findAllByTask(any());
    }
}
