package kg.peaksoft.peaksoftlmsb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.peaksoft.peaksoftlmsb6.dto.request.ForgotPasswordRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.LoginRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.AuthResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Authentication API", description = "User can login and resent password")
public class AuthApi {
    private final UserService authService;

    @PostMapping("/signin")
    @Operation(description = "User can login by email and password")
    public AuthResponse login(@RequestBody LoginRequest request){
        return authService.login(request);
    }

    @PostMapping("/forgot/password")
    @Operation(description = "If the user has forgotten the password, " +
            "he can send an email after he will receive a link to reset the password in the mail")
    public SimpleResponse forgotPassword(@RequestParam String email,
                                         @RequestParam String link) throws MessagingException {
        return authService.forgotPassword(email,link);
    }

    @PostMapping("/reset/password")
    @Operation(description = "User writes a new password then writes again to confirm")
    public SimpleResponse resetPassword(@RequestBody ForgotPasswordRequest newPassword) {
        return authService.resetPassword(newPassword);
    }
}
