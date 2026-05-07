package com.albert.kliniktanggap.service;

import com.albert.kliniktanggap.dto.request.UserRequest;
import com.albert.kliniktanggap.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> findAll();
    UserResponse create(UserRequest request);
    UserResponse update(Long id, UserRequest request);
    void delete(Long id);
    UserResponse findById(Long id);
}
