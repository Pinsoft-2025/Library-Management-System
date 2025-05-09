package io.github.SenaUstun_Dev.library_management.service;

import java.util.List;

import io.github.SenaUstun_Dev.library_management.dto.request.UpdateUserRequest;
import io.github.SenaUstun_Dev.library_management.dto.response.AppUserResponse;
import io.github.SenaUstun_Dev.library_management.entity.AppUser;

public interface UserService {
    AppUserResponse updateUser(Long id, UpdateUserRequest updateUserRequest);
    void deleteUser(Long id);
    List<AppUserResponse> findAllUsers();
    AppUserResponse findUserById(Long id);
    List<AppUserResponse> findUsersByFirstName(String firstName);
    List<AppUserResponse> findUsersByLastName(String lastName);
    AppUserResponse findUserByUsername(String username);
    AppUserResponse findUserByEmail(String email);
    
    // Admin için entity döndüren metotlar
    AppUser findUserEntityById(Long id);
    List<AppUser> findAllUserEntities();
} 