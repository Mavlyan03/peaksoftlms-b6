package kg.peaksoft.peaksoftlmsb6.repository;

import kg.peaksoft.peaksoftlmsb6.dto.response.VideoResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface VideoRepository extends JpaRepository<Video, Long> {

    @Query("select new kg.peaksoft.peaksoftlmsb6.dto.response.VideoResponse(" +
            "v.id," +
            "v.videoName," +
            "v.videoDescription," +
            "v.videoLink) from Video v where v.id = ?1")
    VideoResponse getVideo(Long id);

    @Modifying
    @Transactional
    @Query("update Video set " +
            "videoName = :videoName, " +
            "videoDescription = :description, " +
            "videoLink = :link where id = :id")
    void update(@Param("id") Long id,
                @Param("videoName") String videoName,
                @Param("description") String description,
                @Param("link") String link);

    @Modifying
    @Transactional
    @Query("delete from Video where id = ?1")
    void deleteVideoById(Long id);
}
