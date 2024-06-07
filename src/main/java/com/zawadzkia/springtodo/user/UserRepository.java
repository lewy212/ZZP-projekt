package com.zawadzkia.springtodo.user;

import com.zawadzkia.springtodo.user.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel,Long> {
    UserModel findByUsername(String username);

}
