package kg.peaksoft.peaksoftlmsb6.repository;

import kg.peaksoft.peaksoftlmsb6.entity.Question;
import kg.peaksoft.peaksoftlmsb6.entity.enums.QuestionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Modifying
    @Transactional
    @Query("update Question set " +
            "question = :question, " +
            "questionType = :questionType where id = :id")
    void update(@Param("id") Long id,
                @Param("question") String question,
                @Param("questionType") QuestionType questionType);
}
