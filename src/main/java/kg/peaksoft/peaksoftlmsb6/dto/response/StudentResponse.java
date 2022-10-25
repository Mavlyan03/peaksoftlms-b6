package kg.peaksoft.peaksoftlmsb6.dto.response;

import kg.peaksoft.peaksoftlmsb6.entity.enums.StudyFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@AllArgsConstructor
public class StudentResponse {
    private Long id;
    private String fullName;
    private String groupName;
    @Enumerated(EnumType.STRING)
    private StudyFormat studyFormat;
    private String phoneNumber;
    private String email;
}