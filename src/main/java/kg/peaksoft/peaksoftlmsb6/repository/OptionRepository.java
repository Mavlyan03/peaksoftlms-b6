package kg.peaksoft.peaksoftlmsb6.repository;

import kg.peaksoft.peaksoftlmsb6.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface OptionRepository extends JpaRepository<Option, Long> {

    @Modifying
    @Transactional
    @Query("delete from Option where id = ?1")
    void deleteOptionById(Long id);
}
