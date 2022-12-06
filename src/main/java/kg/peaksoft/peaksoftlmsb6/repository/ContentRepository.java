package kg.peaksoft.peaksoftlmsb6.repository;

import kg.peaksoft.peaksoftlmsb6.entity.Content;
import kg.peaksoft.peaksoftlmsb6.entity.enums.ContentFormat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface ContentRepository extends JpaRepository<Content, Long> {

    @Modifying
    @Transactional
    @Query("update Content set " +
            "contentName = :contentName," +
            "contentFormat = :format," +
            "contentValue = :value where id = :id")
    void update(@Param("id") Long id,
                @Param("contentName") String contentName,
                @Param("format") ContentFormat contentFormat,
                @Param("value") String contentValue);
}