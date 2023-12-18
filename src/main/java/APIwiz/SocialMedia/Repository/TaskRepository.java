package APIwiz.SocialMedia.Repository;

import APIwiz.SocialMedia.Model.Task;
import APIwiz.SocialMedia.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Task> findByTitle(String title);

    List<Task> findByDueDate(LocalDate dueDate);

    List<Task> findByStatus(TaskStatus status);
}
