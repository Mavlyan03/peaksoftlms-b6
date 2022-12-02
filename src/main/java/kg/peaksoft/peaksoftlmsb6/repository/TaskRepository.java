package kg.peaksoft.peaksoftlmsb6.repository;

import kg.peaksoft.peaksoftlmsb6.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Modifying
    @Transactional
    @Query("update Task set " +
            "taskName = :taskName where id = :id")
    void update(@Param("id") Long id,
                @Param("taskName") String taskName);

    @Modifying
    @Transactional
    @Query("delete from Task where id = ?1")
    void deleteTaskById(Long id);
}
