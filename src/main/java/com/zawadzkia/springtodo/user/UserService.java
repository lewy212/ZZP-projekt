package com.zawadzkia.springtodo.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    public List<UserModel> getUsers(){return userRepository.findAll();}
    public UserModel getUser(Long id){return userRepository.getReferenceById(id);}
    public UserModel getUser(String nazwa){return userRepository.findByUsername(nazwa);}
}
