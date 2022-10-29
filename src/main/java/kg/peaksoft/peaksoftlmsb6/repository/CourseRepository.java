package kg.peaksoft.peaksoftlmsb6.repository;

import kg.peaksoft.peaksoftlmsb6.dto.response.AssignInstructorResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.StudentResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.CourseResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.Deque;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Modifying
    @Transactional
    @Query("update Course set " +
            "courseName = :courseName, " +
            "courseDescription = :description, " +
            "dateOfStart = :dateOfStart, " +
            "courseImage = :image where id = :id")
    void update(@Param("id") Long id,
                @Param("courseName") String courseName,
                @Param("description") String description,
                @Param("dateOfStart") LocalDate dateOfStart,
                @Param("image") String image);


    @Query("select new kg.peaksoft.peaksoftlmsb6.dto.response.AssignInstructorResponse(" +
            "i.id," +
            "concat(i.firstName,' ',i.lastName)," +
            "i.specialization," +
            "i.phoneNumber," +
            "i.user.email) from Instructor i where i.id = ?1")
    AssignInstructorResponse getInstructorByCourseId(Long id);

    @Query("select new kg.peaksoft.peaksoftlmsb6.dto.response.CourseResponse(" +
            "c.id," +
            "c.courseName," +
            "c.courseDescription," +
            "c.dateOfStart," +
            "c.courseImage) from Course c where c.id = ?1")
    CourseResponse getCourse(Long id);

    @Query("select new kg.peaksoft.peaksoftlmsb6.dto.response.StudentResponse(" +
            "s.id," +
            "concat(s.firstName,' ',s.lastName)," +
            "s.group.groupName," +
            "s.studyFormat," +
            "s.phoneNumber," +
            "s.email) from Student s where s.id = ?1")
    StudentResponse getStudentByCourseId(Long id);


    @Query("select new kg.peaksoft.peaksoftlmsb6.dto.response.CourseResponse(" +
            "c.id," +
            "c.courseName," +
            "c.courseDescription," +
            "c.dateOfStart," +
            "c.courseImage) from Course c group by c order by c.id desc ")
    ArrayDeque<CourseResponse> getAllCourses();
}