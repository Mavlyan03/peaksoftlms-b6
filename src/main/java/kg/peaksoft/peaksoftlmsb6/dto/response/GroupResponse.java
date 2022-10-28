package kg.peaksoft.peaksoftlmsb6.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class GroupResponse {
    private Long id;
    private String groupName;
    private String description;
    private LocalDate dateOfStart;
    private String image;
}
