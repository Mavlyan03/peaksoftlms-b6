package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.ContentRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.TaskRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.ContentResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.TaskResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Content;
import kg.peaksoft.peaksoftlmsb6.entity.Lesson;
import kg.peaksoft.peaksoftlmsb6.entity.Task;
import kg.peaksoft.peaksoftlmsb6.exception.BadRequestException;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.ContentRepository;
import kg.peaksoft.peaksoftlmsb6.repository.LessonRepository;
import kg.peaksoft.peaksoftlmsb6.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;
    private final LessonRepository lessonRepository;
    private final ContentRepository contentRepository;

    public TaskResponse createTask(TaskRequest request) {
        Task task = convertToEntity(request);
        taskRepository.save(task);
        log.info("New task successfully saved");
        return convertToResponse(task);
    }

    private Task convertToEntity(TaskRequest request) {
        Lesson lesson = lessonRepository.findById(request.getLessonId()).orElseThrow(
                () -> {
                    log.error("Lesson with id {} not found", request.getLessonId());
                    throw new NotFoundException("Урок не найден");
                });
        Task task = null;
        if (lesson.getTask() == null) {
            task = new Task(request);
            lesson.setTask(task);
            task.setLesson(lesson);
            for (ContentRequest contentRequest : request.getContentRequests()) {
                Content content = new Content(
                        contentRequest.getContentName(),
                        contentRequest.getContentFormat(),
                        contentRequest.getContentValue());
                task.addContent(content);
                content.setTask(task);
            }
        } else {
            log.error("Lesson already have a task");
            throw new BadRequestException("У урока уже есть задача");
        }
        return task;
    }

    private TaskResponse convertToResponse(Task task) {
        TaskResponse taskResponse = new TaskResponse(task.getId(), task.getTaskName());
        List<ContentResponse> contentResponses = new ArrayList<>();
        for (Content content : task.getContents()) {
            ContentResponse contentResponse = new ContentResponse(
                    content.getId(),
                    content.getContentName(),
                    content.getContentFormat(),
                    content.getContentValue());
            contentResponses.add(contentResponse);
        }
        taskResponse.setContentResponses(contentResponses);
        return taskResponse;
    }

    public TaskResponse updateTask(Long id, TaskRequest taskRequest) {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Task with id {} not found", id);
                    throw new NotFoundException("Задача не найдена");
                });
        task.setTaskName(taskRequest.getTaskName());
        int i = 0;
        for (ContentRequest contentRequest : taskRequest.getContentRequests()) {
            if (i < task.getContents().size()) {
                Content content = task.getContents().get(i);
                contentRepository.update(
                        content.getId(),
                        contentRequest.getContentName(),
                        contentRequest.getContentFormat(),
                        contentRequest.getContentValue());
                i++;
            } else {
                break;
            }
        }
        log.info("Update task with id {} was successfully", id);
        return convertUpdateResponse(task, taskRequest.getContentRequests());
    }

    public TaskResponse convertUpdateResponse(Task task, List<ContentRequest> contents) {
        TaskResponse taskResponse = new TaskResponse(task);
        List<ContentResponse> contentResponses = new ArrayList<>();
        int i = 0;
        for (ContentRequest content : contents) {
            if (i < task.getContents().size()) {
                Content content1 = task.getContents().get(i);
                contentResponses.add(new ContentResponse(content, content1.getId()));
            } else {
                break;
            }
        }
        taskResponse.setContentResponses(contentResponses);
        return taskResponse;
    }

    public SimpleResponse deleteById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Delete task with id {} was successfully", id);
                    throw new NotFoundException("Задача не найдена");
                });
        for (Content content : task.getContents()) {
            content.setTask(null);
            task.setContents(null);
            contentRepository.deleteById(content.getId());
        }
        taskRepository.deleteTaskById(id);
        log.info("Delete task with id {} was successfully", id);
        return new SimpleResponse("Задача удалена");
    }

    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Get task with id {} was successfully", id);
                    throw new NotFoundException("Задача не найдена");
                });
        log.info("Get task with id {} was successfully", id);
        return convertToResponse(task);
    }
}
