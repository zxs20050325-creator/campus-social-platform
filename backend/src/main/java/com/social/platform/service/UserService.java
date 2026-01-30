package com.social.platform.service;

import com.social.platform.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findByStudentId(String studentId);
    
    User registerUser(String studentId, String nickname, String password, String school, String contactInfo);
}