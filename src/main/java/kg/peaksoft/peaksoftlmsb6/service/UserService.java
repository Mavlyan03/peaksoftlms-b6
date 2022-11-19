package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.ForgotPasswordRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.LoginRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.StudentRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.AuthResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.entity.User;
import kg.peaksoft.peaksoftlmsb6.exception.BadCredentialsException;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.UserRepository;
import kg.peaksoft.peaksoftlmsb6.security.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;


    public AuthResponse login(LoginRequest userRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userRequest.getEmail(),
                        userRequest.getPassword()));
        System.out.println(userRequest.getEmail());
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> {
                    log.error("Bad credentials");
                    throw new BadCredentialsException("Неправильные данные");
                });
        String token = jwtTokenUtil.generateToken(user.getEmail());
        log.info("Login user with email {} and password {} was successfully",
                userRequest.getEmail(), userRequest.getPassword());
        return new AuthResponse(user.getUsername(), token, user.getRole());
    }

    public SimpleResponse forgotPassword(String email, String link) throws MessagingException {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> {
                    log.error("User with email {} not found", email);
                    throw new NotFoundException("Пользователь не найден");
                });
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setSubject("[peaksoftlms-b6] подвердить пароль");
        messageHelper.setFrom("peaksoftlms-b6@gmail.com");
        messageHelper.setTo(email);
        messageHelper.setText(link + "/" + user.getId(), true);
        javaMailSender.send(mimeMessage);
        log.info("Forgot password with email {} was successfully", email);
        return new SimpleResponse("Отправлено в почту");
    }

    public SimpleResponse resetPassword(ForgotPasswordRequest request) {
        User user = userRepository.findById(request.getId()).orElseThrow(
                () -> {
                    log.error("User with id {} not found", request.getId());
                    throw new NotFoundException("Пользователь не найден");
                });
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        log.info("Reset a new password {} was successfully", request.getNewPassword());
        return new SimpleResponse("Пароль обнавлён");
    }
}