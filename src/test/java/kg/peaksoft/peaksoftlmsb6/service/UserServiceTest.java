package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.ForgotPasswordRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.LoginRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.AuthResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.entity.User;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void login() {
        LoginRequest request = new LoginRequest("admin@gmail.com","admin1234");
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new NotFoundException("User not found"));

        AuthResponse login = userService.login(request);

        assertNotNull(login);
        assertEquals(login.getEmail(), request.getEmail());
        assertEquals(login.getRole(), user.getRole());
    }

    @Test
    void resetPassword() {
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setId(1L);
        request.setNewPassword("admin3421");

        User user = userRepository.findById(request.getId()).orElseThrow(
                () -> new NotFoundException("User not found"));
        SimpleResponse response = userService.resetPassword(request);

        assertNotNull(response);
        assertNotEquals(request.getNewPassword(), user.getPassword());
    }
}