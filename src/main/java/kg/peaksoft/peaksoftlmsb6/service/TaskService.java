package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.ContentRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.TaskRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.UpdateContentRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.UpdateTaskRequest;
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
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final LessonRepository lessonRepository;
    private final ContentRepository contentRepository;


    public TaskResponse createTask(TaskRequest request) {
        Task task = convertToEntity(request);
        taskRepository.save(task);
        return convertToResponse(task);
    }

    private Task convertToEntity(TaskRequest request) {
        Lesson lesson = lessonRepository.findById(request.getLessonId()).orElseThrow(
                () -> new NotFoundException("Урок не найден"));
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
        }else {
            throw new BadRequestException("У урока уже есть задача");
        }
        return task;
    }

    private TaskResponse convertToResponse(Task task) {
        TaskResponse taskResponse = new TaskResponse(task.getId(), task.getTaskName());
        List<ContentResponse> contentResponses = new ArrayList<>();
        for(Content content : task.getContents()) {
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


    public SimpleResponse updateTask(Long id, UpdateTaskRequest taskRequest) {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Задача не найдена"));
        taskRepository.update(task.getId(), taskRequest.getTaskName());
        for(UpdateContentRequest contentRequest : taskRequest.getContentRequestList()) {
            convertToEntity(contentRequest);
        }
        return new SimpleResponse("Task update successfully");
    }

    private void convertToEntity(UpdateContentRequest contentRequest) {
        Task task = taskRepository.findById(contentRequest.getTaskId()).orElseThrow(
                () -> new NotFoundException("Задача не найдена"));
        for(Content content : task.getContents()) {
            content.setContentName(contentRequest.getContentName());
            content.setContentFormat(contentRequest.getContentFormat());
            content.setContentValue(contentRequest.getContentValue());
        }
    }


//    private TaskResponse convertUpdateToResponse(Long id, UpdateTaskRequest request) {
//        Task task = taskRepository.findById(id).orElseThrow(
//                () -> new NotFoundException("Задача не найдена"));
//        TaskResponse taskResponse = new TaskResponse(id, request.getTaskName());
//        List<ContentResponse> contentResponses = new ArrayList<>();
//        for(UpdateTaskRequest contentRequest : request.getContentRequestList()) {
//            for(Content content : task.getContents()) {
//                contentResponses.add(new ContentResponse(
//                        content.getId(),
//                        contentRequest.getContentName(),
//                        contentRequest.getContentFormat(),
//                        contentRequest.getContentValue()));
//            }
//        }
//        taskResponse.setContentResponses(contentResponses);
//        return taskResponse;
//    }


    public SimpleResponse deleteById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Задача не найдена"));
        for(Content content : task.getContents()) {
            content.setTask(null);
            task.setContents(null);
            contentRepository.deleteById(content.getId());
        }
        taskRepository.deleteTaskById(id);
        return new SimpleResponse("Задача удалена");
    }

    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Задача не найдена"));
        return convertToResponse(task);
    }

}
