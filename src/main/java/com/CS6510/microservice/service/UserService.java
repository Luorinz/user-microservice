package com.CS6510.microservice.service;

import com.CS6510.microservice.dao.IUserDao;
import com.CS6510.microservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private IUserDao userDao;

    public User getUser(Long userId) throws Exception {
        return null;
    }

    public List<User> getUsers() throws Exception {
        return null;
    }

    public Integer insertUser(User user) throws Exception {
        return null;
    }

    public Integer updateUser() throws Exception {
        return null;
    }

    public Integer deleteUser(Long userId) throws Exception {
        return null;
    }
}
