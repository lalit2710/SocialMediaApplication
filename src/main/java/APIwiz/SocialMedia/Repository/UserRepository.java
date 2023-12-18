package APIwiz.SocialMedia.Repository;

import APIwiz.SocialMedia.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository <User, Long>{
    User findByUsername(String Username);

    List<User> findByRole(String role);

    User findByEmail(String email);

    User findByUsernameAndRole(String username, String role);
}
