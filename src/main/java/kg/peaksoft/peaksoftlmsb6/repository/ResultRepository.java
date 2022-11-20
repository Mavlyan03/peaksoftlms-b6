package kg.peaksoft.peaksoftlmsb6.repository;

import kg.peaksoft.peaksoftlmsb6.dto.response.ResultResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Results;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResultRepository extends JpaRepository<Results, Long> {


    @Query("select new kg.peaksoft.peaksoftlmsb6.dto.response.ResultResponse(" +
            "r.id," +
            "concat(r.student.firstName,' ',r.student.lastName)," +
            "r.amountOfCorrectAnswers," +
            "r.amountOfIncorrectAnswers," +
            "r.percent) from Results r where r.id = ?1")
    ResultResponse getResult(Long id);

    @Query("select r from Results r where r.test.id = ?1")
    List<Results> findResultByTestId(Long id);

    @Query("select r from Results r where r.student.id = ?1")
    Results findResultByStudentsId(Long id);
}
