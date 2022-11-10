package kg.peaksoft.peaksoftlmsb6.repository;

import kg.peaksoft.peaksoftlmsb6.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    @Modifying
    @Transactional
    @Query("update Lesson set lessonName = :lessonName where id = :id")
    void update(@Param("id") Long id,
                @Param("lessonName") String lessonName);

    void deleteLessonById(Long id);
}

