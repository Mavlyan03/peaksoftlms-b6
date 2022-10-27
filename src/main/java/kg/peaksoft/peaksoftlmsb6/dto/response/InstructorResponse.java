package kg.peaksoft.peaksoftlmsb6.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InstructorResponse {
    private Long id;
    private String fullName;
    private String phoneNumber;
    private String specialization;
    private String email;

}
