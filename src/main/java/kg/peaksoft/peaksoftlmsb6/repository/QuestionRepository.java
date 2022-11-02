package kg.peaksoft.peaksoftlmsb6.repository;

import kg.peaksoft.peaksoftlmsb6.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
