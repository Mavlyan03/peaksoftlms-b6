package kg.peaksoft.peaksoftlmsb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.peaksoft.peaksoftlmsb6.dto.request.TaskRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.UpdateTaskRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.TaskResponse;
import kg.peaksoft.peaksoftlmsb6.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/task")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Task API", description = "Task api endpoints")
@PreAuthorize("hasAuthority('INSTRUCTOR')")
public class TaskApi {

    private final TaskService taskService;

    @PostMapping
    @Operation(summary = "Save task",
            description = "To save a new task by Instructor")
    public TaskResponse createTask(@RequestBody TaskRequest taskRequest) {
        return taskService.createTask(taskRequest);
    }


    @PutMapping("/{id}")
    @Operation(summary = "Update task",
            description = "Instructor update task by id")
    public SimpleResponse updateTask(@PathVariable Long id, @RequestBody TaskRequest taskRequest) {
        return taskService.updateTask(id, taskRequest);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task",
            description = "Instructor delete task by id")
    public SimpleResponse deleteTask(@PathVariable Long id) {
        return taskService.deleteById(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by id",
            description = "Instructor get task by id")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','STUDENT')")
    public TaskResponse getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

}
