package com.zawadzkia.springtodo.user;

import com.zawadzkia.springtodo.task.status.TaskStatusDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping({ "/user" })
public class UserController {
    private final UserService userService;
    @GetMapping()
    public String getStatusList(Model model) {
        List<UserDTO> userList = userService.getUsers(); // Call the correct method
        userList.sort(Comparator.comparing(UserDTO::getId));
        model.addAttribute("userList",  userList);
        return "user/list";
    }
    @PostMapping("/delete/{id}")
    public String deleteStatus(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/user";
    }
}
