package com.zawadzkia.springtodo.user;

import com.zawadzkia.springtodo.auth.Authority;
import com.zawadzkia.springtodo.auth.AuthorityRepository;
import com.zawadzkia.springtodo.task.status.TaskStatusDTO;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    //public List<UserModel> getUsers(){return userRepository.findAll();}
    public UserModel getUser(Long id){return userRepository.getReferenceById(id);}
    public UserModel getUser(String nazwa){return userRepository.findByUsername(nazwa);}
    public List<UserDTO> getUsers()
    {
        List<UserDTO> result = new ArrayList<>();
        List<UserModel> users = userRepository.findAll();

        for (UserModel user : users) {
            boolean isAdmin = false;
            for (Authority authority : user.getAuthorities()) {
                if (authority.getAuthority().equals("ADMIN")) {
                    isAdmin = true;
                    break;
                }
            }
            if (!isAdmin) {
                result.add(new UserDTO(user.getId(), user.getUsername(), user.getPassword()));
            }
        }

        return result;
    }
    public void addUser(UserDTO userDTO){
        UserModel userModel = new UserModel();
        userModel.setUsername(userDTO.getLogin());
        userModel.setPassword("{noop}"+userDTO.getPassword());
        userModel.getAuthorities().add(authorityRepository.findByAuthority("USER"));
        userModel.setEnabled(true);
        userRepository.save(userModel);
    }

    public void delete(Long id) {
        UserModel userModel = userRepository.getReferenceById(id);
        userRepository.delete(userModel);
    }

    public void updateUser(UserDTO userDTO) {
        System.out.println("Nie wchodze");
        UserModel userModel = userRepository.getReferenceById(userDTO.getId());
        userModel.setUsername(userDTO.getLogin());
        userModel.setPassword("{noop}"+userDTO.getPassword());
        userRepository.save(userModel);
    }
}
