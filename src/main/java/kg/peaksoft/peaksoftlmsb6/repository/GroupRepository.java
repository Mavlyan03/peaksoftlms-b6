package kg.peaksoft.peaksoftlmsb6.repository;

import kg.peaksoft.peaksoftlmsb6.dto.response.GroupResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.StudentResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Group;
import kg.peaksoft.peaksoftlmsb6.entity.Results;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {

    @Modifying
    @Transactional
    @Query("update Group set " +
            "groupName = :groupName," +
            "groupDescription = :description," +
            "dateOfStart = :dateOfStart," +
            "groupImage = :image where id = :id")
    void update(@Param("id") Long id,
                @Param("groupName")String groupName,
                @Param("description")String description,
                @Param("dateOfStart")LocalDate dateOfStart,
                @Param("image")String image);

    @Query("select new kg.peaksoft.peaksoftlmsb6.dto.response.GroupResponse(" +
            "g.id," +
            "g.groupName," +
            "g.groupDescription," +
            "g.dateOfStart," +
            "g.groupImage) from Group g where g.id = ?1")
    GroupResponse getGroup(Long id);

    @Query("select new kg.peaksoft.peaksoftlmsb6.dto.response.GroupResponse(" +
            "g.id," +
            "g.groupName," +
            "g.groupDescription," +
            "g.dateOfStart," +
            "g.groupImage) from Group g")
    List<GroupResponse> getAllGroups();

    @Query("select new kg.peaksoft.peaksoftlmsb6.dto.response.StudentResponse(" +
            "s.id," +
            "concat(s.firstName,' ',s.lastName)," +
            "s.group.groupName," +
            "s.studyFormat," +
            "s.phoneNumber," +
            "s.email ) from Student s where s.group.id = ?1")
    List<StudentResponse> getStudentsByGroupId(Long id);

}