package kg.peaksoft.peaksoftlmsb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.peaksoft.peaksoftlmsb6.dto.request.GroupRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.GroupResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.StudentResponse;
import kg.peaksoft.peaksoftlmsb6.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Deque;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/group")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Group API", description = "ADMIN group api endpoints")
@PreAuthorize("hasAuthority('ADMIN')")
public class GroupApi {

    private final GroupService groupService;

    @PostMapping
    @Operation(summary = "Save group",
            description = "To save group by ADMIN")
    public GroupResponse createGroup(@RequestBody GroupRequest request) {
        return groupService.createGroup(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Updates group",
            description = "Admin update group by id")
    public GroupResponse updateGroup(@PathVariable Long id,
                                @RequestBody GroupRequest request) {
        return groupService.updateGroup(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes group ",
            description = "Admin delete the group by id")
    public SimpleResponse deleteGroup(@PathVariable Long id) {
        return groupService.deleteById(id);
    }

    @GetMapping
    @Operation(summary = "Gets all groups",
            description = "Admin get all groups")
    public List<GroupResponse> getAllGroups() {
        return groupService.getAllGroups();
    }

    @GetMapping("/students/{id}")
    @Operation(summary = "Get all students from group",
            description = "Get all students from group by id")
    public List<StudentResponse> getAllStudents(@PathVariable Long id) {
        return groupService.getAllStudentsFromGroup(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get group by id",
            description = "Get group by id for admin")
    public GroupResponse getGroupById(@PathVariable Long id) {
        return groupService.getById(id);
    }

}
