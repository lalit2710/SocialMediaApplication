package APIwiz.SocialMedia.Controller;

import APIwiz.SocialMedia.Model.Task;
import APIwiz.SocialMedia.Model.User;
import APIwiz.SocialMedia.Service.TaskService;
import APIwiz.SocialMedia.Service.UserService;
import APIwiz.SocialMedia.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    public final TaskService taskService;
    public final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        taskService.setDueDateAutomatically(task);
        if (taskService.validateTaskDetails(task)) {
            Task createdTask = taskService.createTask(task);
            return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @RequestBody Task updatedTask) {
        Task existingTask = taskService.updateTask(taskId, updatedTask);
        if (existingTask != null) {
            return new ResponseEntity<>(existingTask, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        boolean deleted = taskService.deleteTask(taskId);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long taskId) {
        Task task = taskService.getTaskById(taskId);
        if (task != null) {
            return new ResponseEntity<>(task, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping("/{taskId}/assign/{userId}")
    public ResponseEntity<Task> assignTaskToUser(@PathVariable Long taskId, @PathVariable Long userId) {
        Task task = taskService.getTaskById(taskId); // Fetch the task by its ID
        User user = userService.getUserById(userId); // Fetch the user by their ID

        if (task != null && user != null) {
            taskService.assignTaskToUser(task, user); // Assign the task to the user
            return new ResponseEntity<>(taskService.updateTask(taskId, task), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Task or user not found
        }
    }
    @GetMapping("/title/{title}")
    public ResponseEntity<List<Task>> getTasksByTitle(@PathVariable String title) {
        List<Task> tasks = taskService.findTasksByTitle(title);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/due-date/{dueDate}")
    public ResponseEntity<List<Task>> getTasksByDueDate(@PathVariable LocalDate dueDate) {
        List<Task> tasks = taskService.findTasksByDueDate(dueDate);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable TaskStatus status) {
        List<Task> tasks = taskService.findTasksByStatus(status);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

}
