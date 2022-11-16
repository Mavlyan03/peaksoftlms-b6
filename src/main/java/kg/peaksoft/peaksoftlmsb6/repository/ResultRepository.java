package kg.peaksoft.peaksoftlmsb6.repository;

import kg.peaksoft.peaksoftlmsb6.entity.Results;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ResultRepository extends JpaRepository<Results, Long> {

    @Query("select r from Results r where r.student.id = ?1")
    Results findResultByStudentsId(Long id);
}
