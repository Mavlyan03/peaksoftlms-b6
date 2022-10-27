package kg.peaksoft.peaksoftlmsb6.dto.request;

import kg.peaksoft.peaksoftlmsb6.entity.enums.StudyFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
public class StudentRequest {
    private Long id;

    private String firstName;

    private String lastName;

    private Long groupId;

    @Enumerated(EnumType.STRING)
    private StudyFormat studyFormat;

    private String phoneNumber;

    private String email;

    private  String password;
}
