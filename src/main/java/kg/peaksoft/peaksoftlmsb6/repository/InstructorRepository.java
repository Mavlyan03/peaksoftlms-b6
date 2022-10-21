package kg.peaksoft.peaksoftlmsb6.repository;

import kg.peaksoft.peaksoftlmsb6.dto.response.InstructorResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    @Query("select new kg.peaksoft.peaksoftlmsb6.dto.response.InstructorResponse(" +
            "i.id,"+
            "concat(i.firstName,' ',i.lastName) ," +
            "i.phoneNumber," +
            "i.specialization," +
            "i.user.email) from Instructor i")
    List<InstructorResponse> getAllInstructors();
}