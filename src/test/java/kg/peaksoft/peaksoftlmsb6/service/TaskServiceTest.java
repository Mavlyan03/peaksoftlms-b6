package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.ContentRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.TaskRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.TaskResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Task;
import kg.peaksoft.peaksoftlmsb6.entity.enums.ContentFormat;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void createTask() {
        TaskRequest request = new TaskRequest();
        request.setTaskName("MVC");
        request.setLessonId(7L);
        List<ContentRequest> contents = new ArrayList<>();
        ContentRequest contentRequest = new ContentRequest();
        contentRequest.setContentName("link");
        contentRequest.setContentFormat(ContentFormat.LINK);
        contentRequest.setContentValue("link about interview");
        contents.add(contentRequest);
        request.setContentRequests(contents);

        TaskResponse task = taskService.createTask(request);

        assertNotNull(task);
        assertEquals(task.getTaskName(), request.getTaskName());
        assertEquals(task.getContentResponses().listIterator().next().getContentName(),
                request.getContentRequests().listIterator().next().getContentName());
        assertEquals(task.getContentResponses().listIterator().next().getContentFormat(),
                request.getContentRequests().listIterator().next().getContentFormat());
        assertEquals(task.getContentResponses().listIterator().next().getContentValue(),
                request.getContentRequests().listIterator().next().getContentValue());
    }

    @Test
    void updateTask() {
        TaskRequest request = new TaskRequest();
        request.setTaskName("Migration");
        request.setLessonId(7L);
        List<ContentRequest> contents = new ArrayList<>();
        ContentRequest contentRequest = new ContentRequest();
        contentRequest.setContentName("video");
        contentRequest.setContentFormat(ContentFormat.LINK);
        contentRequest.setContentValue("video about interview");
        contents.add(contentRequest);
        request.setContentRequests(contents);

        Task task = taskRepository.findById(2L).orElseThrow(() -> new NotFoundException("Task not found"));
        TaskResponse response = taskService.updateTask(task.getId(), request);

        assertNotNull(response);
        assertEquals(task.getTaskName(), request.getTaskName());
    }

    @Test
    void deleteById() {
        SimpleResponse simpleResponse = taskService.deleteById(1L);

        assertNotNull(simpleResponse);
        assertThatThrownBy(() -> taskService.getTaskById(1L)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void getTaskById() {
        TaskResponse task = taskService.getTaskById(2L);

        assertNotNull(task);
        assertEquals(task.getId(), 2L);
    }
}