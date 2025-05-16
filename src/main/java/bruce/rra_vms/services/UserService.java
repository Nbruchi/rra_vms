package bruce.rra_vms.services;

import bruce.rra_vms.dto.AuthRequest;
import bruce.rra_vms.dto.AuthResponse;
import bruce.rra_vms.dto.UserResponse;
import bruce.rra_vms.errors.EmailAlreadyExistsException;
import bruce.rra_vms.errors.InvalidCredentialsException;
import bruce.rra_vms.models.User;
import bruce.rra_vms.repos.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final JwtService jwtService;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final ModelMapper modelMapper;

    public UserService(JwtService jwtService, UserRepo userRepo, PasswordEncoder passwordEncoder, AuthenticationManager authManager, ModelMapper modelMapper) {
        this.jwtService = jwtService;
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
    }

    public AuthResponse registerUser(AuthRequest request) {
        if (userRepo.existsByEmail(request.email())){
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User user = new User(request.email());
        user.setRole(request.role());
        user.setPassword(passwordEncoder.encode(request.password()));

        // Set additional fields
        user.setNames(request.names());
        user.setNationalId(request.nationalId());
        user.setPhone(request.phone());

        userRepo.save(user);

        return new AuthResponse(user.getId(), user.getUsername(), user.getRole(), null);
    }

    public AuthResponse loginUser(AuthRequest request) {
        try{
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );

            User user = userRepo.findByEmail(request.email()).orElseThrow(() ->
                    new InvalidCredentialsException("Invalid credentials"));

            String token = jwtService.generateToken(user);

            return new AuthResponse(user.getId(), user.getUsername(), user.getRole(), token);
        }catch (AuthenticationException ex){
            throw new InvalidCredentialsException("Invalid credentials");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserResponse> getAllUsers(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<User> savedUsers = userRepo.findAll(pageable);
        return savedUsers.map(user -> modelMapper.map(user, UserResponse.class));
    }
}
