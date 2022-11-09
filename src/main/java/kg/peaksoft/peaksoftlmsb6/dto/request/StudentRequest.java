package kg.peaksoft.peaksoftlmsb6.dto.request;

import kg.peaksoft.peaksoftlmsb6.entity.enums.StudyFormat;
import kg.peaksoft.peaksoftlmsb6.validation.PasswordValid;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class StudentRequest {

    @NotNull(message = "First name must be not null")
    private String firstName;
    private String lastName;
    private Long groupId;
    @Enumerated(EnumType.STRING)
    private StudyFormat studyFormat;
    private String phoneNumber;
    @Email
    @NotNull(message = "Email be must not null")
    private String email;
    @PasswordValid
    @NotNull(message = "Password be must not null")
    private  String password;
}
