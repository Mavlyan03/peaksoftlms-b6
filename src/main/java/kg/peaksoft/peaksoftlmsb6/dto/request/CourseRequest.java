package kg.peaksoft.peaksoftlmsb6.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CourseRequest {
    private String courseName;
    private String description;
    private LocalDate dateOfStart;
    private String image;
}
