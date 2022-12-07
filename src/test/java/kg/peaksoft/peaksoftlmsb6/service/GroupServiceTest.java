package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.GroupRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.GroupResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.StudentResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Group;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.GroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class GroupServiceTest {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupService groupService;

    @Test
    void testCreateGroup() {
        GroupRequest request = new GroupRequest();
        request.setGroupName("groupName");
        request.setDescription("description");
        request.setDateOfStart(LocalDate.of(2020, 1, 1));
        request.setImage("image");

        GroupResponse group = groupService.createGroup(request);

        assertNotNull(group);
        assertEquals(request.getGroupName(), group.getGroupName());
        assertEquals(request.getDescription(), group.getDescription());
        assertEquals(request.getImage(), group.getImage());
        assertEquals(request.getDateOfStart(), group.getDateOfStart());
    }

    @Test
    void testUpdateGroup() {
        GroupRequest request = new GroupRequest();
        request.setGroupName("Java-6");
        request.setDescription("IT");
        request.setImage("java.com");
        request.setDateOfStart(LocalDate.of(2022,12,31));

        Group group = groupRepository.findById(1L).orElseThrow(
                () -> new NotFoundException("Group not found"));
        GroupResponse response = groupService.updateGroup(group.getId(), request);

        assertNotNull(response);
        assertEquals(response.getGroupName(), request.getGroupName());
        assertEquals(response.getDescription(), request.getDescription());
        assertEquals(response.getImage(), request.getImage());
        assertEquals(response.getDateOfStart(), request.getDateOfStart());
    }

    @Test
    void testDeleteGroup() {
        SimpleResponse simpleResponse = groupService.deleteById(1L);

        assertNotNull(simpleResponse);
        assertThatThrownBy(() -> groupService.getById(1L)).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Группа не найдена");
    }

    @Test
    void testGetAllGroups() {
        List<GroupResponse> result = groupService.getAllGroups();

        assertEquals(2, result.size());
    }

    @Test
    void testGetById() {
        GroupResponse group = groupService.getById(1L);

        assertNotNull(group);
        assertEquals(group.getId(), 1L);
    }

    @Test
    void testGetAllStudentsFromGroup() {
        List<StudentResponse> result = groupService.getAllStudentsFromGroup(1L);

        assertEquals(3,result.size());
    }
}