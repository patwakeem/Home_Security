package com.interactivehome.main_service.service.user;

import com.interactivehome.main_service.model.user.dto.UserDto;
import com.interactivehome.main_service.model.user.entity.User;

import java.util.List;

public interface UserService {
    void registerUser(UserDto dto);
    User modifyUser(Integer id, UserDto dto);
    User getUserById(Integer id);
    List<User> getAllUsers();
    Boolean deleteUser(Integer id);
}
