package com.zawadzkia.springtodo.user;

import com.zawadzkia.springtodo.task.status.TaskStatusDTO;
import com.zawadzkia.springtodo.task.status.TaskStatusModel;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping({ "/user" })
public class UserController {
    private final UserService userService;
    @GetMapping()
    public String getUserList(Model model) {
        List<UserDTO> userList = userService.getUsers(); // Call the correct method
        userList.sort(Comparator.comparing(UserDTO::getId));
        model.addAttribute("userList",  userList);
        return "user/list";
    }

    @GetMapping("/add")
    public String addUser(Model model) {
        model.addAttribute("user", new UserDTO());
        return "user/create";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/create";
        }
        userService.addUser(userDTO);
        return "redirect:/user";
    }

    @GetMapping("/edit/{id}")
    public String editUserView(@PathVariable Long id, Model model) {
        UserModel usermodel = userService.getUser(id);

        UserDTO userDTO = new UserDTO(usermodel.getId(), usermodel.getUsername(),usermodel.getPassword());
        if (userDTO.getPassword().startsWith("{noop}")) {
            userDTO.setPassword(userDTO.getPassword().substring(6));
        }
        model.addAttribute("user", userDTO);
        return "user/edit";
    }

    @PostMapping("/edit")
    public String editUser(@ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println("Mam errora");
            return "user/edit";
        }
        System.out.println("Jestem tutaj");
        userService.updateUser(userDTO);
        return "redirect:/user";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/user";
    }
}
