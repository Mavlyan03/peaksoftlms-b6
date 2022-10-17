package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.ForgotPasswordRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.LoginRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.AuthResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.entity.User;
import kg.peaksoft.peaksoftlmsb6.exception.BadCredentialsException;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.UserRepository;
import kg.peaksoft.peaksoftlmsb6.security.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
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
public class UserService  {
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
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new BadCredentialsException("bad credentials"));
        String token = jwtTokenUtil.generateToken(user.getEmail());
        return new AuthResponse(user.getUsername(),token,user.getRole());
    }


    public SimpleResponse forgotPassword(String email, String link) throws MessagingException {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException("User not found"));
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,true,"UTF-8");
        messageHelper.setSubject("[peaksoftlms-b6] reset password link");
        messageHelper.setFrom("peaksoftlms-b6@gmail.com");
        messageHelper.setTo(email);
        messageHelper.setText(link + "/" + user.getId(), true);
        javaMailSender.send(mimeMessage);
        return new SimpleResponse("Email send");
    }

    public SimpleResponse resetPassword(ForgotPasswordRequest request) {
        User user = userRepository.findById(request.getId()).orElseThrow(
                () -> new NotFoundException("User with id not found"));
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        return new SimpleResponse("password updated");
    }

}

