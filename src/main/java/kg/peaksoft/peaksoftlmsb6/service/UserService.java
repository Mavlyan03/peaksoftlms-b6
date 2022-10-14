package kg.peaksoft.peaksoftlmsb6.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import kg.peaksoft.peaksoftlmsb6.dto.request.LoginRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.AuthResponse;
import kg.peaksoft.peaksoftlmsb6.entity.User;
import kg.peaksoft.peaksoftlmsb6.repository.UserRepository;
import kg.peaksoft.peaksoftlmsb6.security.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService  {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;


    @PostConstruct
    public void init() throws IOException {
        GoogleCredentials googleCredentials =
                GoogleCredentials.fromStream(new ClassPathResource("peaksoft-lms-6.json").getInputStream());

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(googleCredentials)
                .build();

        FirebaseApp.initializeApp(options);
    }

    public AuthResponse login(LoginRequest userRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userRequest.getEmail(),
                        userRequest.getPassword()));

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new BadCredentialsException("bad credentials"));
        String token = jwtTokenUtil.generateToken(user.getEmail());
        return new AuthResponse(user.getUsername(),token,user.getRole());
    }


}

