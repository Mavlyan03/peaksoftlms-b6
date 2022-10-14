package kg.peaksoft.peaksoftlmsb6.api;

import kg.peaksoft.peaksoftlmsb6.dto.request.LoginRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.AuthResponse;
import kg.peaksoft.peaksoftlmsb6.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthApi {
    private final UserService authService;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request){
        return authService.login(request);
    }
}
