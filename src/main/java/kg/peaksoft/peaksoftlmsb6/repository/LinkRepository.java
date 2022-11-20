package kg.peaksoft.peaksoftlmsb6.repository;

import kg.peaksoft.peaksoftlmsb6.dto.response.LinkResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface LinkRepository extends JpaRepository<Link, Long> {

    @Modifying
    @Transactional
    @Query("update Link set " +
            "linkText = :text, " +
            "link = :link where id = :id")
    void update(@Param("id") Long id,
                @Param("text") String linkText,
                @Param("link") String link);

    @Query("select new kg.peaksoft.peaksoftlmsb6.dto.response.LinkResponse(" +
            "l.id," +
            "l.linkText," +
            "l.link) from Link l where l.id = ?1")
    LinkResponse getLink(Long id);

    @Modifying
    @Transactional
    @Query("delete from Link where id = ?1")
    void deleteLinkById(Long id);

}
