package ro.foame.employee_backend.controllers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.foame.employee_backend.dtos.*;
import ro.foame.employee_backend.emails.EmailVerificationToken;
import ro.foame.employee_backend.entities.Role;
import ro.foame.employee_backend.entities.User;
import ro.foame.employee_backend.repositories.UserRepository;
import ro.foame.employee_backend.services.AuthenticationService;
import ro.foame.employee_backend.services.JwtService;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<RegisterResponseDto> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        RegisterResponseDto registerResponseDto = new RegisterResponseDto(registeredUser.getId(), registeredUser.getFullName(), registeredUser.getEmail(), registeredUser.getUsername(), registeredUser.getAuthorities());

        return ResponseEntity.ok(registerResponseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        if (!authenticatedUser.isEnabled()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Email not verified. Please verify your email before logging in.");
        }

        String jwtToken = jwtService.generateToken(authenticatedUser);

        List<String> roleNames = authenticatedUser.getRoles().stream().map(Role::getName).toList();

        LoginResponseDto loginResponseDto = new LoginResponseDto(
                jwtToken,
                jwtService.getExpirationTime(),
                authenticatedUser.getFullName(),
                roleNames
        );

        return ResponseEntity.ok(loginResponseDto);
    }
    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        boolean verified = authenticationService.verifyUserEmail(token);
        if (!verified) {
            return ResponseEntity.badRequest().body("Invalid or expired token");
        }
        return ResponseEntity.ok("Email verified successfully!");
    }
}