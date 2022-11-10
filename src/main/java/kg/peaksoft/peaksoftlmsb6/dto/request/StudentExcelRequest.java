package kg.peaksoft.peaksoftlmsb6.dto.request;

import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelRow;
import kg.peaksoft.peaksoftlmsb6.entity.enums.StudyFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StudentExcelRequest {

    @ExcelRow
    private int indexRow;

    @ExcelCellName("Name")
    private  String name;

    @ExcelCellName("Last Name")
    private String lastName;

    @ExcelCellName("Phone Number")
    private String phoneNumber;

    @ExcelCellName("Study Format")
    private StudyFormat studyFormat;

    @ExcelCellName("Email")
    private String email;

}