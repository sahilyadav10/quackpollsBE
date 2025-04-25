package com.sahilten.quackpolls.controllers;

import com.sahilten.quackpolls.domain.dto.user.UserDto;
import com.sahilten.quackpolls.domain.entities.UserEntity;
import com.sahilten.quackpolls.domain.mappers.UserMapper;
import com.sahilten.quackpolls.security.user.QuackpollUserDetails;
import com.sahilten.quackpolls.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserMapper userMapper;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserDto> getUser(@AuthenticationPrincipal QuackpollUserDetails userDetails) {

        String email = userDetails.getUsername();

        UserEntity userEntity = userService.get(email);
        UserDto userDto = userMapper.toDto(userEntity);

        return ResponseEntity.ok(userDto);
    }

}
