package com.example.devtask_backend.auth;
import java.util.Objects;
import java.util.Optional;

import com.example.devtask_backend.user.LoginRequest;
import com.example.devtask_backend.user.LoginResponse;
import com.example.devtask_backend.user.UserRepository;
import com.example.devtask_backend.user.User;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse login(LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        Optional<User> foundUser = userRepository.findByUsername(username);

        if (foundUser.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        User user = foundUser.get();

        if (!Objects.equals(user.getPassword(), password)) {
            throw new IllegalArgumentException("비밀번호를 잘못 입력하였습니다.");
        }

        String token = jwtUtil.createToken(user.getId(), user.getUsername());

        return new LoginResponse(token, user.getUsername());
    }
}
