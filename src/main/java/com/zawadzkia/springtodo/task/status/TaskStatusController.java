package com.zawadzkia.springtodo.task.status;

import com.zawadzkia.springtodo.task.TaskDTO;
import com.zawadzkia.springtodo.task.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/status")
@RequiredArgsConstructor
public class TaskStatusController {

    private final TaskStatusService taskStatusService;
    private final TaskService taskService;

    @GetMapping()
    public String getStatusList(Model model) {
        List<TaskStatusDTO> statusList = taskStatusService.getStatuses(); // Call the correct method
        statusList.sort(Comparator.comparing(TaskStatusDTO::getId));
        model.addAttribute("statusList", statusList);
        return "status/list";
    }

    @GetMapping("/add")
    public String addStatusView(Model model) {
        model.addAttribute("status", new TaskStatusModel());
        return "status/create";
    }

    @PostMapping("/add")
    public String addStatus(@ModelAttribute("status") TaskStatusModel status, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "status/create";
        }
        taskStatusService.addStatusWithUser(status);
        return "redirect:/status";
    }

    @PostMapping("/delete/{id}")
    public String deleteStatus(@PathVariable Long id) {
        taskStatusService.delete(id);
        return "redirect:/status";
    }

    @PostMapping(value = "/{id}")
    public String updateTask(@PathVariable Long id, @ModelAttribute("status") TaskStatusDTO taskStatusDTO) {
        TaskDTO taskDTO = taskService.getTaskDTOById(id);
        taskDTO.setStatus(taskStatusDTO);
        taskService.update(taskDTO);
        return "task/list";
    }
}
