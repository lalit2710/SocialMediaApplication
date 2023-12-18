package APIwiz.SocialMedia.Model;

import APIwiz.SocialMedia.TaskStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
    @Table(name = "tasks")
    public class Task {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
    @Column(name = "task_title")
        private String title;
    @Column(nullable = false, length = 100)
        private String description;
        private LocalDate dueDate;

        @Enumerated(EnumType.STRING)
        private TaskStatus status;

        @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
        @JoinColumn(name = "user_id")
        private User user; // Establishes a many-to-one relationship with User entity

}
