package APIwiz.SocialMedia.Service;

import APIwiz.SocialMedia.Exeptions.taskserviceexception;
import APIwiz.SocialMedia.Model.Task;
import APIwiz.SocialMedia.Model.User;
import APIwiz.SocialMedia.Repository.TaskRepository;
import APIwiz.SocialMedia.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    public final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }
    public Task createTask(Task task) {
        try {
            setDueDateAutomatically(task);
            if (validateTaskDetails(task)) {
                return taskRepository.save(task);
            } else {
                throw new IllegalArgumentException("Invalid task details");
            }
        } catch (Exception ex) {
            throw new taskserviceexception("Error occurred while creating task", ex);
        }
    }
    public void setDueDateAutomatically(Task task) {
        if (task.getDueDate() == null) {
            LocalDate defaultDueDate = LocalDate.now().plusDays(7); // For example, set a week from today
            task.setDueDate(defaultDueDate);
        }
    }

    public Task updateTask(Long taskId, Task updatedTask) {
        Task existingTask = taskRepository.findById(taskId).orElse(null);
        if (existingTask != null) {
            existingTask.setTitle(updatedTask.getTitle());
            existingTask.setDescription(updatedTask.getDescription());
            existingTask.setDueDate(updatedTask.getDueDate());
            existingTask.setStatus(updatedTask.getStatus());
            return taskRepository.save(existingTask);
        }
        return null; // if task is not found
    }

    public boolean deleteTask(Long taskId) {
        Task existingTask = taskRepository.findById(taskId).orElse(null);
        if (existingTask != null) {
            taskRepository.delete(existingTask);
            return true;
        }
        return false;// if task is not found or deletion fails
    }

    public boolean validateTaskDetails(Task task) {
        // Add validation logic based on your requirements
        return !task.getTitle().isEmpty() && task.getDescription().length() <= 255;
    }

    public void assignTaskToUser(Task task, User user) {
        task.setUser(user);
    }
    public List<Task> findTasksByTitle(String title) {
        return taskRepository.findByTitle(title);
    }

    public List<Task> findTasksByDueDate(LocalDate dueDate) {
        return taskRepository.findByDueDate(dueDate);
    }

    public List<Task> findTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }
}
