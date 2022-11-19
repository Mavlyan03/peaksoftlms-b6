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
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Authentication API", description = "User can login and resent password")
public class AuthApi {

    private final UserService authService;

    @PostMapping("/login")
    @Operation(summary = "Login",
            description = "User can login by email and password")
    public AuthResponse login(@RequestBody LoginRequest request){
        return authService.login(request);
    }

    @PostMapping("/forgot/password")
    @Operation(summary = "Forgot password",
            description = "Send email if user forgot password")
    public SimpleResponse forgotPassword(@RequestParam String email,
                                         @RequestParam String link) throws MessagingException {
        return authService.forgotPassword(email,link);
    }

    @PostMapping("/reset/password")
    @Operation(summary = "Reset password",
            description = "For save a new password")
    public SimpleResponse resetPassword(@RequestBody @Valid ForgotPasswordRequest newPassword) {
        return authService.resetPassword(newPassword);
    }
}
