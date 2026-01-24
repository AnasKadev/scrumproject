package org.example.scrum.service;

import lombok.RequiredArgsConstructor;
import org.example.scrum.dto.ProjectReportDTO;
import org.example.scrum.dto.SprintReportDTO;
import org.example.scrum.entities.*;
import org.example.scrum.entities.enums.SprintStatus;
import org.example.scrum.entities.enums.TaskStatus;
import org.example.scrum.entities.enums.UserStoryStatus;
import org.example.scrum.exception.ResourceNotFoundException;
import org.example.scrum.repository.ProjectRepository;
import org.example.scrum.repository.SprintBacklogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportingService {

    private final SprintBacklogRepository sprintBacklogRepository;
    private final ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public SprintReportDTO generateSprintReport(Long sprintBacklogId) {
        SprintBacklog sprint = sprintBacklogRepository.findById(sprintBacklogId)
                .orElseThrow(() -> new ResourceNotFoundException("Sprint Backlog non trouvé avec l'ID: " + sprintBacklogId));

        SprintReportDTO report = new SprintReportDTO();
        report.setSprintId(sprint.getId());
        report.setSprintName(sprint.getName());
        report.setStartDate(sprint.getStartDate());
        report.setEndDate(sprint.getEndDate());

        List<UserStory> userStories = sprint.getUserStories();
        report.setTotalUserStories(userStories.size());

        long completedUS = userStories.stream()
                .filter(us -> us.getStatus() == UserStoryStatus.USER_STORY_STATUS_COMPLETED)
                .count();
        long inProgressUS = userStories.stream()
                .filter(us -> us.getStatus() == UserStoryStatus.USER_STORY_STATUS_IN_PROGRESS)
                .count();
        long todoUS = userStories.stream()
                .filter(us -> us.getStatus() == UserStoryStatus.USER_STORY_STATUS_ACTIVE)
                .count();

        report.setCompletedUserStories((int) completedUS);
        report.setInProgressUserStories((int) inProgressUS);
        report.setTodoUserStories((int) todoUS);
        report.setUserStoriesCompletionRate(
            userStories.isEmpty() ? 0.0 : (completedUS * 100.0) / userStories.size()
        );

        List<Task> tasks = sprint.getTasks();
        report.setTotalTasks(tasks.size());

        long completedTasks = tasks.stream()
                .filter(t -> t.getStatus() == TaskStatus.DONE)
                .count();
        long inProgressTasks = tasks.stream()
                .filter(t -> t.getStatus() == TaskStatus.IN_PROGRESS)
                .count();
        long todoTasks = tasks.stream()
                .filter(t -> t.getStatus() == TaskStatus.TO_DO)
                .count();

        report.setCompletedTasks((int) completedTasks);
        report.setInProgressTasks((int) inProgressTasks);
        report.setTodoTasks((int) todoTasks);
        report.setTasksCompletionRate(
            tasks.isEmpty() ? 0.0 : (completedTasks * 100.0) / tasks.size()
        );

        int totalStoryPoints = userStories.stream()
                .mapToInt(us -> us.getStoryPoints() != null ? us.getStoryPoints() : 0)
                .sum();
        int completedStoryPoints = userStories.stream()
                .filter(us -> us.getStatus() == UserStoryStatus.USER_STORY_STATUS_COMPLETED)
                .mapToInt(us -> us.getStoryPoints() != null ? us.getStoryPoints() : 0)
                .sum();

        report.setTotalStoryPoints(totalStoryPoints);
        report.setCompletedStoryPoints(completedStoryPoints);
        report.setRemainingStoryPoints(totalStoryPoints - completedStoryPoints);
        report.setVelocity(completedStoryPoints);


        double totalEstimated = tasks.stream()
                .mapToDouble(t -> t.getEstimatedHours() != null ? t.getEstimatedHours() : 0.0)
                .sum();
        double totalActual = tasks.stream()
                .mapToDouble(t -> t.getActualHours() != null ? t.getActualHours() : 0.0)
                .sum();
        double totalRemaining = tasks.stream()
                .mapToDouble(t -> t.getRemainingHours() != null ? t.getRemainingHours() : 0.0)
                .sum();

        report.setTotalEstimatedHours(totalEstimated);
        report.setTotalActualHours(totalActual);
        report.setTotalRemainingHours(totalRemaining);

        // Génération du Burndown Chart
        report.setBurndownData(generateBurndownData(sprint, totalEstimated));

        return report;
    }


    private Map<LocalDate, Double> generateBurndownData(SprintBacklog sprint, double totalEstimatedHours) {
        Map<LocalDate, Double> burndownData = new HashMap<>();

        if (sprint.getStartDate() == null || sprint.getEndDate() == null) {
            return burndownData;
        }

        LocalDate currentDate = sprint.getStartDate();
        LocalDate endDate = sprint.getEndDate();
        long totalDays = ChronoUnit.DAYS.between(currentDate, endDate);

        if (totalDays <= 0) {
            burndownData.put(currentDate, totalEstimatedHours);
            return burndownData;
        }

        // Ligne idéale - décroissance linéaire
        double dailyBurnRate = totalEstimatedHours / totalDays;
        double remainingHours = totalEstimatedHours;

        while (!currentDate.isAfter(endDate)) {
            burndownData.put(currentDate, Math.max(0, remainingHours));
            remainingHours -= dailyBurnRate;
            currentDate = currentDate.plusDays(1);
        }

        return burndownData;
    }


    @Transactional(readOnly = true)
    public ProjectReportDTO generateProjectReport(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Projet non trouvé avec l'ID: " + projectId));

        ProjectReportDTO report = new ProjectReportDTO();
        report.setProjectId(project.getId());
        report.setProjectName(project.getName());


        if (project.getProductBacklog() != null) {
            ProductBacklog productBacklog = project.getProductBacklog();
            report.setTotalEpics(productBacklog.getEpics().size());
            report.setTotalUserStories(productBacklog.getUserStories().size());

            long prioritized = productBacklog.getUserStories().stream()
                    .filter(us -> us.getPriorityOrder() != null && us.getPriorityOrder() > 0)
                    .count();
            report.setPrioritizedUserStories((int) prioritized);
        } else {
            report.setTotalEpics(0);
            report.setTotalUserStories(0);
            report.setPrioritizedUserStories(0);
        }

        // Statistiques Sprints
        List<SprintBacklog> sprints = project.getSprintBacklogs();
        report.setTotalSprints(sprints.size());

        long completed = sprints.stream()
                .filter(s -> s.getStatus() == SprintStatus.COMPLETED)
                .count();
        long active = sprints.stream()
                .filter(s -> s.getStatus() == SprintStatus.ACTIVE)
                .count();
        long planned = sprints.stream()
                .filter(s -> s.getStatus() == SprintStatus.PLANNED)
                .count();

        report.setCompletedSprints((int) completed);
        report.setActiveSprints((int) active);
        report.setPlannedSprints((int) planned);

        // Vélocité moyenne
        List<Integer> velocities = sprints.stream()
                .filter(s -> s.getStatus() == SprintStatus.COMPLETED)
                .map(this::calculateSprintVelocity)
                .collect(Collectors.toList());

        report.setSprintVelocities(velocities);

        double avgVelocity = velocities.isEmpty() ? 0.0 :
            velocities.stream().mapToInt(Integer::intValue).average().orElse(0.0);
        report.setAverageVelocity(avgVelocity);

        // Membres de l'équipe
        report.setTotalTeamMembers(project.getProjectMembers().size());
        report.setActiveDevelopers(project.getProjectMembers().size()); // Tous sont actifs par défaut

        return report;
    }


    private Integer calculateSprintVelocity(SprintBacklog sprint) {
        return sprint.getUserStories().stream()
                .filter(us -> us.getStatus() == UserStoryStatus.USER_STORY_STATUS_COMPLETED)
                .mapToInt(us -> us.getStoryPoints() != null ? us.getStoryPoints() : 0)
                .sum();
    }


    @Transactional(readOnly = true)
    public List<SprintReportDTO> getSprintHistory(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Projet non trouvé avec l'ID: " + projectId));

        return project.getSprintBacklogs().stream()
                .filter(sprint -> sprint.getStatus() == SprintStatus.COMPLETED)
                .map(sprint -> generateSprintReport(sprint.getId()))
                .collect(Collectors.toList());
    }
}

