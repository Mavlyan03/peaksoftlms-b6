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
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final ResultRepository resultRepository;

    public GroupResponse createGroup(GroupRequest request) {
        Group group = new Group(request);
        groupRepository.save(group);
        return groupRepository.getGroup(group.getId());
    }

    public SimpleResponse deleteById(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Group with id %s not found", id)));
        for (Course course : group.getCourses()) {
            if (course != null) {
                course.getGroup().remove(group);
            }
        }
        for (Student student : group.getStudents()) {
            if (resultRepository.findResultByStudentsId(student.getId()) != null) {
                Results results = resultRepository.findResultByStudentsId(student.getId());
                results.setStudent(null);
            }
        }
        groupRepository.delete(group);
        return new SimpleResponse("Group deleted");
    }

    public GroupResponse updateGroup(Long id, GroupRequest request) {
        Group group = groupRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Group with id %s not found", id)));
        groupRepository.update(
                group.getId(),
                request.getGroupName(),
                request.getDescription(),
                request.getDateOfStart(),
                request.getImage());
        return new GroupResponse(
                group.getId(),
                request.getGroupName(),
                request.getDescription(),
                request.getDateOfStart(),
                request.getImage());
    }

    public List<StudentResponse> getAllStudentsFromGroup(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Group with id %s not found", id)));
        return groupRepository.getStudentsByGroupId(group.getId());
    }

    public List<GroupResponse> getAllGroups() {
        return groupRepository.getAllGroups();
    }

    public GroupResponse getById(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Группа с id = %s не найдена", id)));
        return groupRepository.getGroup(group.getId());
    }

}