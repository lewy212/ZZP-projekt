package com.zawadzkia.springtodo.task.category;

import com.zawadzkia.springtodo.auth.AppUserDetails;
import com.zawadzkia.springtodo.task.status.TaskStatusModel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskCategoryService {
    private final TaskCategoryRepository taskCategoryRepository;

    public ArrayList<TaskCategoryDTO> getCategories()
    {
        ArrayList<TaskCategoryDTO> result = new ArrayList<>();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof AppUserDetails userDetails) {
            List<TaskCategoryModel> allByOwner = taskCategoryRepository.findAllByOwner(userDetails.getUser());
            allByOwner.forEach(category -> result.add(new TaskCategoryDTO(category.getId(), category.getName(), category.getDescription(), category.getImage())));
        }
        return result;
        //return taskCategoryRepository.findAllByOwner(userModel);
    }
    public TaskCategoryModel getTaskCategoryModelByNazwaAndUser(String nazwa)
    {
        TaskCategoryModel result = new TaskCategoryModel();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof AppUserDetails userDetails) {
            TaskCategoryModel byOwner = taskCategoryRepository.findByNameAndOwner(nazwa,userDetails.getUser());
            result = byOwner;
        }
        return result;
    }
}
