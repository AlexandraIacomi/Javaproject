package ro.foame.employee_backend.services.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ro.foame.employee_backend.dtos.LoginUserDto;
import ro.foame.employee_backend.dtos.RegisterUserDto;
import ro.foame.employee_backend.emails.EmailVerificationToken;
import ro.foame.employee_backend.entities.Role;
import ro.foame.employee_backend.entities.User;
import ro.foame.employee_backend.repositories.RoleRepository;
import ro.foame.employee_backend.repositories.UserRepository;

import java.util.Optional;
import java.util.Set;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final RoleRepository roleRepository;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public User signup(RegisterUserDto input) {
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        User user = new User();
        user.setFullName(input.getFullName());
        user.setEmail(input.getEmail());
        user.setUsername(input.getUsername());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setEnabled(false);
        user.setRoles(Set.of(userRole));

        User savedUser = userRepository.save(user);

        String token = EmailVerificationToken.createToken(savedUser.getId().longValue());
        String verificationLink = "http://localhost:8080/auth/verify-email?token=" + token;

        System.out.println("Mock email sent to: " + savedUser.getEmail());
        System.out.println("Click to verify: " + verificationLink);

        return savedUser;
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }

    public boolean verifyUserEmail(String token) {
        Long userId = EmailVerificationToken.getUserId(token);
        if (userId == null) {
            return false;
        }

        Optional<User> optionalUser = userRepository.findById(userId.intValue());
        if (optionalUser.isEmpty()) {
            return false;
        }

        User user = optionalUser.get();
        user.setEnabled(true);
        userRepository.save(user);

        EmailVerificationToken.invalidateToken(token);

        return true;
    }
}