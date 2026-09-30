package offer_tracker.service;

import lombok.RequiredArgsConstructor;
import offer_tracker.dto.AuthRequest;
import offer_tracker.dto.AuthResponse;
import offer_tracker.entity.User;
import offer_tracker.exception.InvalidCredentialsException;
import offer_tracker.exception.UsernameAlreadyExistsException;
import offer_tracker.repository.UserRepository;
import offer_tracker.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse register(AuthRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new UsernameAlreadyExistsException(request.username());
        }
        User user = new User();
        user.setUsername(request.username());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        userRepository.save(user);
        return new AuthResponse(jwtService.generateToken(user.getId()));
    }

    @Transactional(readOnly = true)
    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByUsername(request.username())
                .filter(u -> passwordEncoder.matches(request.password(), u.getPasswordHash()))
                .orElseThrow(InvalidCredentialsException::new);
        return new AuthResponse(jwtService.generateToken(user.getId()));
    }
}