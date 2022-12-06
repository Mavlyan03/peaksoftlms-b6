package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.GroupRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.GroupResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.StudentResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Course;
import kg.peaksoft.peaksoftlmsb6.entity.Group;
import kg.peaksoft.peaksoftlmsb6.entity.Results;
import kg.peaksoft.peaksoftlmsb6.entity.Student;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.GroupRepository;
import kg.peaksoft.peaksoftlmsb6.repository.ResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class GroupService {

    private final GroupRepository groupRepository;

    private final ResultRepository resultRepository;

    public GroupResponse createGroup(GroupRequest request) {
        Group group = new Group(request);
        groupRepository.save(group);
        log.info("New course successfully saved!");
        return groupRepository.getGroup(group.getId());
    }

    public SimpleResponse deleteById(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Group with id {} not found", id);
                    throw new NotFoundException("Группа не найдена");
                });
        for (Course course : group.getCourses()) {
            if (course != null) {
                course.getGroup().remove(group);
            }
        }
        for (Student student : group.getStudents()) {
            if (resultRepository.findResultByStudentsId(student.getId()) != null) {
                Results results = resultRepository.findResultByStudentsId(student.getId());
                results.setTest(null);
                resultRepository.deleteById(results.getId());
            }
        }
        groupRepository.delete(group);
        log.info("Delete group by id {} was successfully", id);
        return new SimpleResponse("Группа удалена");
    }

    public GroupResponse updateGroup(Long id, GroupRequest request) {
        Group group = groupRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Group with id {} not found", id);
                    throw new NotFoundException("Группа не найдена");
                });
        groupRepository.update(
                group.getId(),
                request.getGroupName(),
                request.getDescription(),
                request.getDateOfStart(),
                request.getImage());
        log.info("Update group with id {} was successfully", id);
        return new GroupResponse(
                group.getId(),
                request.getGroupName(),
                request.getDescription(),
                request.getDateOfStart(),
                request.getImage());
    }

    public List<StudentResponse> getAllStudentsFromGroup(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Group with id {} not found", id);
                    throw new NotFoundException("Группа не найдена");
                });
        log.info("Get all students from course by id {} was successfully", id);
        return groupRepository.getStudentsByGroupId(group.getId());
    }

    public List<GroupResponse> getAllGroups() {
        log.info("Get all groups was successfully");
        return groupRepository.getAllGroups();
    }

    public GroupResponse getById(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Group with id {} not found", id);
                    throw new NotFoundException("Группа не найдена");
                });
        log.info("Get group by id {} was successfully", id);
        return groupRepository.getGroup(group.getId());
    }
}