package com.example.devtask_backend.user;

import com.example.devtask_backend.common.NotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse signup(SignupRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username은 필수입니다.");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("password는 필수입니다");
        }

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("이미 존재하는 username입니다.");
        }

        String encodedPassword = passwordEncoder.encode(password);

        User user = new User(username, encodedPassword);

        User savedUser = userRepository.save(user);

        return new UserResponse(savedUser.getId(), savedUser.getUsername());
    }

    public UserResponse getMe(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        return new UserResponse(user.getId(), user.getUsername());
    }
}
