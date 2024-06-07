package com.zawadzkia.springtodo.task;

import com.zawadzkia.springtodo.task.category.TaskCategoryModel;
import com.zawadzkia.springtodo.task.category.TaskCategoryRepository;
import com.zawadzkia.springtodo.task.category.TaskCategoryService;
import com.zawadzkia.springtodo.task.status.TaskStatusDTO;
import com.zawadzkia.springtodo.task.status.TaskStatusModel;
import com.zawadzkia.springtodo.task.status.TaskStatusService;
import com.zawadzkia.springtodo.user.UserModel;
import com.zawadzkia.springtodo.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.config.Task;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping({ "/task" })
@Slf4j
class TaskController {

    private final TaskService taskService;

    private final TaskStatusService taskStatusService;
    private final UserService userService;
    private final TaskCategoryService taskCategoryService;

    @GetMapping()
    String getTaskList(Model model) {
        List<TaskDTO> taskList = taskService.getTaskList();
        List<TaskStatusDTO> userTaskStatusList = taskStatusService.getUserTaskStatusList();
        model.addAttribute("tasks", taskList);
        model.addAttribute("statusList", userTaskStatusList);
        return "task/list";

    }

    @GetMapping("/add")
    public String addTaskForm(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserModel loggedUser = userService.getUser(authentication.getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String startDate = LocalDateTime.now().format(formatter);
        String dueDate = LocalDateTime.now().plusHours(20).format(formatter);

        model.addAttribute("task",new TaskDTO());
        model.addAttribute("startDate", startDate);
        model.addAttribute("dueDate", dueDate);
        model.addAttribute("categories",taskCategoryService.getCategories());
        model.addAttribute("statuses",taskStatusService.getUserTaskStatusList());
        return "task/create";
    }

    @PostMapping("/add")
    public String addTask(@ModelAttribute ("task") TaskDTO dto,
                          BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "redirect:/add";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserModel loggedUser = userService.getUser(authentication.getName());

        TaskCategoryModel category = taskCategoryService.getTaskCategoryModelByNazwaAndUser(dto.getCategory().getName());
        TaskStatusModel status = taskStatusService.getTaskStatusModelByNazwaAndUser(dto.getStatus().getName());


        TaskModel task = new TaskModel();
        task.setOwner(userService.getUser(loggedUser.getId()));
        task.setSummary(dto.getSummary());
        task.setDescription(dto.getDescription());
        task.setStartDate(dto.getStartDate());
        task.setDueDate(dto.getDueDate());
        task.setCategory(category);
        task.setStatus(status);
        taskService.save(task);


        return "redirect:/task/list";
    }

    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id){
        taskService.delete(id);
        return "reditect:/task/list";
    }


}
