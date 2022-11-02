package kg.peaksoft.peaksoftlmsb6.repository;

import kg.peaksoft.peaksoftlmsb6.dto.response.PresentationResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Presentation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface PresentationRepository extends JpaRepository<Presentation, Long> {

    @Query("select new kg.peaksoft.peaksoftlmsb6.dto.response.PresentationResponse(" +
            "p.id," +
            "p.presentationName," +
            "p.presentationDescription," +
            "p.presentationLink) from Presentation p where p.id = ?1")
    PresentationResponse getPresentation(Long id);

    @Modifying
    @Transactional
    @Query("update Presentation set " +
            "presentationName = :presentationName, " +
            "presentationDescription = :description, " +
            "presentationLink = :link where id = :id")
    void update(@Param("id") Long id,
                @Param("presentationName") String presentationName,
                @Param("description") String description,
                @Param("link") String link);

    @Modifying
    @Transactional
    @Query("delete from Presentation where id = ?1")
    void deletePresentationById(Long id);
}
