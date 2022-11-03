package kg.peaksoft.peaksoftlmsb6.repository;

import kg.peaksoft.peaksoftlmsb6.dto.response.StudentResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Instructor;
import kg.peaksoft.peaksoftlmsb6.entity.Student;
import kg.peaksoft.peaksoftlmsb6.entity.enums.StudyFormat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("select s from Student s where s.user.email = :email")
    Optional<Student> findByEmail(@Param("email") String email);

    @Query("select new kg.peaksoft.peaksoftlmsb6.dto.response.StudentResponse(" +
            "id," +
            "concat(firstName,' ',lastName)," +
            "group.groupName," +
            "studyFormat," +
            "phoneNumber," +
            "user.email) from Student order by concat(firstName,' ',lastName)")
    List<StudentResponse> getAllStudents();

    @Modifying
    @Transactional
    @Query("update Student s set " +
            "s.firstName = :firstName, " +
            "s.lastName = :lastName, " +
            "s.studyFormat = :studyFormat, " +
            "s.phoneNumber = :phoneNumber"
//            "s.user.email = :email
            + " where s.id = :id")
    void update(@Param("id") Long id,
                @Param("firstName") String firstName,
                @Param("lastName") String lastName,
                @Param("studyFormat") StudyFormat studyFormat,
                @Param("phoneNumber") String phoneNumber);

    @Query("select new kg.peaksoft.peaksoftlmsb6.dto.response.StudentResponse(" +
            "s.id," +
            "concat(s.firstName,' ', s.lastName)," +
            "s.group.groupName," +
            "s.studyFormat," +
            "s.phoneNumber," +
            "s.user.email)from Student s where s.id = ?1")
    StudentResponse getStudent(Long id);


    @Query("select new kg.peaksoft.peaksoftlmsb6.dto.response.StudentResponse(s.id, concat(s.firstName, ' ', s.lastName)," +
            "s.group.groupName, s.studyFormat, s.phoneNumber, s.user.email) from Student s " +
            "where s.studyFormat = :studyFormat order by concat(s.firstName,' ',s.lastName)")
    List<StudentResponse> findStudentByStudyFormat(@Param("studyFormat") StudyFormat studyFormat);

    @Query("select s from Student s where s.user.id = ?1")
    Optional<Student> findByUserId(Long id);
}

