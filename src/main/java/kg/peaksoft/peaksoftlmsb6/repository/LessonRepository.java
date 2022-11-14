package kg.peaksoft.peaksoftlmsb6.repository;

import kg.peaksoft.peaksoftlmsb6.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    @Modifying
    @Transactional
    @Query("delete from Lesson l where l.course.id = ?1")
    void deleteLessonById(Long id);

}

