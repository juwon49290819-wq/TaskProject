package com.example.devtask_backend.auth;
import java.util.Optional;

import com.example.devtask_backend.common.UnauthorizedException;
import com.example.devtask_backend.user.LoginRequest;
import com.example.devtask_backend.user.LoginResponse;
import com.example.devtask_backend.user.UserRepository;
import com.example.devtask_backend.user.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username은 필수입니다.");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("password는 필수입니다.");
        }

        Optional<User> foundUser = userRepository.findByUsername(username);

        if (foundUser.isEmpty()) {
            throw new UnauthorizedException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        User user = foundUser.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        String token = jwtUtil.createToken(user.getId(), user.getUsername());

        return new LoginResponse(token, user.getUsername());
    }

    public Long getUserIdFromAuthorization(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }
        String token = authorization.substring(7);

        if (!jwtUtil.validateToken(token)) {
            throw new UnauthorizedException("유효하지 않은 토큰입니다.");
        }

        return jwtUtil.getUserId(token);
    }
}
