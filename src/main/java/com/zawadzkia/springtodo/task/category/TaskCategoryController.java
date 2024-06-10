package com.zawadzkia.springtodo.task.category;

import com.zawadzkia.springtodo.auth.AppUserDetails;
import com.zawadzkia.springtodo.auth.Authority;
import com.zawadzkia.springtodo.task.TaskDTO;
import com.zawadzkia.springtodo.task.status.TaskStatusDTO;
import com.zawadzkia.springtodo.user.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof AppUserDetails userDetails) {
            String roles = userDetails.getUser().getAuthorities().stream()
                    .map(Authority::getAuthority)
                    .collect(Collectors.joining(", "));
            System.out.println("User roles: " + roles);
        }
       // System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
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
