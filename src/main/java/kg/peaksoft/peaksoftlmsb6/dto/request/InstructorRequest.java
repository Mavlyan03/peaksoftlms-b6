package kg.peaksoft.peaksoftlmsb6.dto.request;

import kg.peaksoft.peaksoftlmsb6.validation.PasswordValid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor

public class InstructorRequest {
    @NotNull
    private String firstName;
    private String lastName;
    private String phoneNumber;
    @Email
    private String email;
    private String specialization;
    @PasswordValid
    private String password;
}