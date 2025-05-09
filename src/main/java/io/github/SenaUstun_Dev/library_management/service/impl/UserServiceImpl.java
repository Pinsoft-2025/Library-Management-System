package io.github.SenaUstun_Dev.library_management.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.SenaUstun_Dev.library_management.dto.request.UpdateUserRequest;
import io.github.SenaUstun_Dev.library_management.dto.response.AppUserResponse;
import io.github.SenaUstun_Dev.library_management.entity.AppUser;
import io.github.SenaUstun_Dev.library_management.exception.BaseException;
import io.github.SenaUstun_Dev.library_management.exception.ErrorMessages;
import io.github.SenaUstun_Dev.library_management.repository.AppUserRepository;
import io.github.SenaUstun_Dev.library_management.service.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final AppUserRepository userRepository;

    @Override
    public AppUserResponse updateUser(Long id, UpdateUserRequest updateUserRequest) {
        AppUser user = findUserEntityById(id);
        
        user.setUsername(updateUserRequest.username());
        user.setEmail(updateUserRequest.email());
        user.setFirstName(updateUserRequest.firstName());
        user.setLastName(updateUserRequest.lastName());
        
        return convertToDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        AppUser user = findUserEntityById(id);
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppUserResponse> findAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AppUserResponse findUserById(Long id) {
        return convertToDto(findUserEntityById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppUserResponse> findUsersByFirstName(String firstName) {
        return userRepository.findByFirstNameContainingIgnoreCase(firstName).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppUserResponse> findUsersByLastName(String lastName) {
        return userRepository.findByLastNameContainingIgnoreCase(lastName).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AppUserResponse findUserByUsername(String username) {
        return convertToDto(userRepository.findByUsername(username)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND, ErrorMessages.RESOURCE_NOT_FOUND, "Kullanıcı bulunamadı, Kullanıcı adı: " + username)));
    }

    @Override
    @Transactional(readOnly = true)
    public AppUserResponse findUserByEmail(String email) {
        return convertToDto(userRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND, ErrorMessages.RESOURCE_NOT_FOUND, "Kullanıcı bulunamadı, E-posta: " + email)));
    }
    
    // Admin için entity döndüren metodlar
    @Override
    @Transactional(readOnly = true)
    public AppUser findUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND, ErrorMessages.RESOURCE_NOT_FOUND, "Kullanıcı bulunamadı, ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppUser> findAllUserEntities() {
        return userRepository.findAll();
    }


    //>>>>>>>>>>>>>>>>>>HELPER METHOD
    // Entity'den DTO'ya dönüştürme metodu
    private AppUserResponse convertToDto(AppUser user) {
        return new AppUserResponse(
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName()
        );
    }
} 