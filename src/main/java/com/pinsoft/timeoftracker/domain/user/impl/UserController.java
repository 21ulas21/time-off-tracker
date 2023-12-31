package com.pinsoft.timeoftracker.domain.user.impl;

import com.pinsoft.timeoftracker.domain.user.PasswordChangeRequest;
import com.pinsoft.timeoftracker.domain.user.UserRoleRequest;
import com.pinsoft.timeoftracker.domain.user.api.UserDto;
import com.pinsoft.timeoftracker.domain.user.api.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserController {


    private final UserService service;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable(value = "id") String id) {
        var result = service.getUserById(id);
        return ResponseEntity.ok(UserResponse.fromDto(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable(value = "id") String id,
                                                   @RequestBody UserRequest request) {
        var result = service.updateUser(id, request.toDto());
        return ResponseEntity.ok(UserResponse.fromDto(result));
    }

    @GetMapping("/get-manager-users")
    public ResponseEntity<List<UserResponse>> getUserByManagerRole() {

        List<UserResponse> userResponse = service.getUserByManagerRole()
                .stream()
                .map(UserResponse::fromDto)
                .toList();
        return ResponseEntity.ok(userResponse);
    }
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<UserResponse> updateUserRole(@PathVariable(value = "id") String id, @RequestBody UserRoleRequest userRole){
        UserRole request = UserRole.valueOf(userRole.getUserRole().toUpperCase());
        UserDto user = service.changeUserRole(id, request);
        return ResponseEntity.ok(UserResponse.fromDto(user));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable(value = "id")String id){
        service.deleteUserById(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping()
    public ResponseEntity<UserResponse> getAuthenticatedUser(){
        var user = service.getAuthenticatedUser();
        return ResponseEntity.ok(UserResponse.fromDto(user));
    }
    @PostMapping("/password-changes")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody PasswordChangeRequest request){
        service.changePassword(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUser(){
        List<UserResponse> users = service.getAllUser().stream().map(UserResponse::fromDto).toList();
        return ResponseEntity.ok(users);

    }

}
