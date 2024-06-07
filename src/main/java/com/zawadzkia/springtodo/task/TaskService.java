package com.zawadzkia.springtodo.task;

import com.zawadzkia.springtodo.task.category.TaskCategoryDTO;
import com.zawadzkia.springtodo.task.category.TaskCategoryModel;
import com.zawadzkia.springtodo.task.status.TaskStatusDTO;
import com.zawadzkia.springtodo.task.status.TaskStatusModel;
import com.zawadzkia.springtodo.task.status.TaskStatusRepository;
import com.zawadzkia.springtodo.user.UserModel;
import com.zawadzkia.springtodo.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TaskStatusRepository taskStatusRepository;

    public List<TaskDTO> getTaskList() {
        UserModel user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Set<TaskModel> tasks = user.getTasks();
        List<TaskDTO> result = new ArrayList<>();
        tasks.forEach(taskModel -> {
            TaskStatusModel status = taskModel.getStatus();
            TaskCategoryModel category = taskModel.getCategory();
            TaskStatusDTO taskStatusDTO = new TaskStatusDTO(status.getId(), status.getName(), status.getDisplayName());
            TaskCategoryDTO taskCategoryDTO = new TaskCategoryDTO(category.getId(), category.getName(), category.getDescription(), category.getImage());
            TaskDTO taskDTO = new TaskDTO(taskModel.getId(), taskModel.getSummary(), taskModel.getDescription(),
                    taskModel.getStartDate(), taskModel.getDueDate(), taskModel.getDescription(), taskStatusDTO,taskCategoryDTO);
            result.add(taskDTO);
        });
        return result;
    }

    public TaskDTO getTaskDTOById(Long id) {
        TaskModel taskModel = taskRepository.getReferenceById(id);
        TaskStatusModel status = taskModel.getStatus();
        TaskCategoryModel category = taskModel.getCategory();
        TaskStatusDTO taskStatusDTO = new TaskStatusDTO(status.getId(), status.getName(), status.getDisplayName());
        TaskCategoryDTO taskCategoryDTO = new TaskCategoryDTO(category.getId(), category.getName(), category.getDescription(), category.getImage());
        TaskDTO taskDTO = new TaskDTO(taskModel.getId(), taskModel.getSummary(), taskModel.getDescription(),
                taskModel.getStartDate(), taskModel.getDueDate(), taskModel.getDescription(), taskStatusDTO,taskCategoryDTO);
        return taskDTO;
    }


    public TaskModel getTaskModelById(Long id) {
        TaskModel taskModel = taskRepository.getReferenceById(id);
        return taskModel;
    }

    public void update(TaskDTO taskDTO) {
        TaskModel taskModel = taskRepository.findById(taskDTO.getId()).orElseThrow();
        taskModel.setSummary(taskDTO.getSummary());
        taskModel.setDescription(taskDTO.getDescription());
        taskModel.setStartDate(taskDTO.getStartDate());
        taskModel.setDueDate(taskDTO.getDueDate());
        taskModel.setAttachment(taskDTO.getAttachment());
        taskModel.setStatus(taskStatusRepository.getReferenceById(taskDTO.getStatus().getId()));
        taskRepository.save(taskModel);
    }

    public void delete(Long id){
        TaskModel taskModel = taskRepository.getReferenceById(id);
        taskRepository.delete(taskModel);
    }
    public void save(TaskModel taskModel){
        taskRepository.save(taskModel);
    }

}
