package bruce.rra_vms.controllers;

import bruce.rra_vms.dto.AuthRequest;
import bruce.rra_vms.dto.AuthResponse;
import bruce.rra_vms.dto.UserResponse;
import bruce.rra_vms.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "User Management", description = "APIs for user registration, authentication, and management")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account with the provided details")
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(userService.registerUser(authRequest));
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticates a user and returns a JWT token")
    public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(userService.loginUser(authRequest));
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users", description = "Retrieves all users with pagination (admin only)")
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getAllUsers(page, size));
    }
}
