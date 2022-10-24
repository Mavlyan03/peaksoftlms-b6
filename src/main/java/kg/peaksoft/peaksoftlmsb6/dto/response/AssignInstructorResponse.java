package kg.peaksoft.peaksoftlmsb6.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AssignInstructorResponse {
    private Long id;
    private String fullName;
    private String specialization;
    private String phoneNumber;
    private String email;
}