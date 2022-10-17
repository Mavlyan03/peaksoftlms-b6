package kg.peaksoft.peaksoftlmsb6.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordRequest {
    private Long id;
    private String newPassword;
}
