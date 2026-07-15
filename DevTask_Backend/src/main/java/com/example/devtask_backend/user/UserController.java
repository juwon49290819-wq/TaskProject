package com.example.devtask_backend.user;

import com.example.devtask_backend.auth.AuthService;
import com.example.devtask_backend.auth.JwtUtil;
import com.example.devtask_backend.common.UnauthorizedException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/signup")
    public UserResponse signup(@RequestBody SignupRequest request) {
        return userService.signup(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public UserResponse me(@RequestHeader("Authorization") String authorization) {
        Long userId = authService.getUserIdFromAuthorization(authorization);

        return userService.getMe(userId);
    }
}
