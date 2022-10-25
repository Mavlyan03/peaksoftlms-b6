package kg.peaksoft.peaksoftlmsb6.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class CourseResponse {
    private Long id;
    private String courseName;
    private String description;
    private LocalDate dateOfStart;
    private String image;
}
