package kg.peaksoft.peaksoftlmsb6.dto.response;

import kg.peaksoft.peaksoftlmsb6.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {
    private String email;
    private String token;
    private Role role;
}
