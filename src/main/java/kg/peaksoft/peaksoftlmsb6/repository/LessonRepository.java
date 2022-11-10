package kg.peaksoft.peaksoftlmsb6.repository;

import com.sun.istack.Nullable;
import kg.peaksoft.peaksoftlmsb6.dto.response.LessonResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Lesson;
import net.bytebuddy.utility.nullability.AlwaysNull;
import net.bytebuddy.utility.nullability.MaybeNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import javax.validation.constraints.Null;
import java.util.List;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    @Modifying
    @Transactional
    @Query("update Lesson set lessonName = :lessonName where id = :id")
    void update(@Param("id") Long id,
                @Param("lessonName") String lessonName);



}