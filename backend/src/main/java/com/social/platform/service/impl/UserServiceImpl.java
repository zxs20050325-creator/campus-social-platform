package com.social.platform.service.impl;

import com.social.platform.entity.User;
import com.social.platform.repository.UserRepository;
import com.social.platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String studentId) throws UsernameNotFoundException {
        User user = userRepository.findByStudentId(studentId);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with student id: " + studentId);
        }
        return user;
    }

    @Override
    public User findByStudentId(String studentId) {
        return userRepository.findByStudentId(studentId);
    }

    @Override
    public User registerUser(String studentId, String nickname, String password, String school, String contactInfo) {
        // 检查学号是否已经存在
        if (userRepository.findByStudentId(studentId) != null) {
            throw new RuntimeException("Student ID already exists: " + studentId);
        }

        User user = new User();
        user.setStudentId(studentId);
        user.setNickname(nickname);
        user.setPassword(passwordEncoder.encode(password)); // 加密密码
        user.setSchool(school);
        user.setContactInfo(contactInfo);

        return userRepository.save(user);
    }
}