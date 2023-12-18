package APIwiz.SocialMedia.Service;

import APIwiz.SocialMedia.Exeptions.UserNotFoundException;
import APIwiz.SocialMedia.Model.User;
import APIwiz.SocialMedia.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public User registerUser(User user) {
        // Check if the username already exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Validate email format using a regular expression (this is a basic example)
        if (!isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }

        // Validate password strength (e.g., minimum length, special characters, etc.)
        if (!isValidPassword(user.getPassword())) {
            throw new IllegalArgumentException("Password does not meet requirements");
        }

        // Hash password before storing in the database
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        // Set default role (e.g., USER)
        user.setRole("USER");

        // Save the user in the database
        return userRepository.save(user);
    }

    // Additional helper methods for validation

    private boolean isValidEmail(String email) {
        // Implement email format validation using regex or libraries like Apache Commons Validator
        // Example regex for basic email format validation
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    private boolean isValidPassword(String password) {
        // Implement password strength validation logic (e.g., minimum length, special characters, etc.)
        // Example: Check if the password length is at least 8 characters
        return password.length() >= 8;
    }
//    public User registerUser(User user) {
//        // Hash password before storing in the database
//        String hashedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
//        user.setPassword(hashedPassword);
//
//        // Set default role (e.g., USER)
//        user.setRole("USER");
//
//        // Save the user in the database
//        return userRepository.save(user);
//    }

    public  User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public List<User>getAllUsers(){
        return userRepository.findAll();
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public User updateUser(Long userId, User updatedUser) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        // Update user details
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setRole(updatedUser.getRole());

        return userRepository.save(existingUser);
    }

    public boolean deleteUserById(Long userId) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        userRepository.delete(existingUser);
        return true;
    }

    public List<User> getUsersByRole(String role) {
        return userRepository.findByRole(role);
    }

    public boolean validateUserCredentials(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            // Logic to validate user credentials
            return password.equals(user.getPassword());
        }
        return false;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserByUsernameAndRole(String username, String role) {
        return userRepository.findByUsernameAndRole(username, role);
    }
}
