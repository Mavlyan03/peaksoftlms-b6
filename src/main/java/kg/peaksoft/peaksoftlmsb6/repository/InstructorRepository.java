package kg.peaksoft.peaksoftlmsb6.repository;

import kg.peaksoft.peaksoftlmsb6.dto.response.InstructorResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Instructor;
import kg.peaksoft.peaksoftlmsb6.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    @Query("select new kg.peaksoft.peaksoftlmsb6.dto.response.InstructorResponse(" +
            "i.id," +
            "concat(i.firstName,' ',i.lastName) ," +
            "i.phoneNumber," +
            "i.specialization," +
            "i.user.email) from Instructor i")
    List<InstructorResponse> getAllInstructors();

    @Modifying
    @Transactional
    @Query("update Instructor set " +
            "firstName = :firstName," +
            "lastName = :lastName," +
            "specialization = :specialization," +
            "phoneNumber = :phoneNumber " +
            "where id = :id")
    void update(@Param("id") Long id,
                @Param("firstName") String firstName,
                @Param("lastName") String lastName,
                @Param("specialization") String specialization,
                @Param("phoneNumber") String phoneNumber);

    @Query("select new kg.peaksoft.peaksoftlmsb6.dto.response.InstructorResponse(" +
            "i.id," +
            "concat(i.firstName,' ',i.lastName)," +
            "i.phoneNumber," +
            "i.specialization," +
            "i.user.email) from Instructor i where i.id = ?1")
    InstructorResponse getInstructor(Long id);

    @Query("select i from Instructor i where i.user.id = ?1")
    Optional<Instructor> findByUserId(Long id);
}