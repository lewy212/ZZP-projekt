package com.zawadzkia.springtodo.task.category;

import com.zawadzkia.springtodo.task.TaskDTO;
import com.zawadzkia.springtodo.task.status.TaskStatusDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping({ "/category" })
public class TaskCategoryController {

    private final TaskCategoryService taskCategoryService;
    @GetMapping()
    String getCategoryList(Model model) {
        List<TaskCategoryDTO> categoryList = taskCategoryService.getCategories();
        categoryList.sort(Comparator.comparing(TaskCategoryDTO::getId));
        model.addAttribute("categoryList", categoryList);
        return "category/list";

    }
    @GetMapping("/add")
    public String addCategoryView(Model model) {
        model.addAttribute("category", new TaskCategoryModel());
        return "category/create";
    }

    @PostMapping("/add")
    public String addCategory(@ModelAttribute("category") TaskCategoryModel category, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "redirect:/add";
        }
        taskCategoryService.addCategoryWithUser(category);
        return "redirect:/category";
    }
    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id){
        taskCategoryService.delete(id);
        return "redirect:/category";
    }
}
