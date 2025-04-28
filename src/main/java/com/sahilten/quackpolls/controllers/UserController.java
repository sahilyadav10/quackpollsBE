package com.sahilten.quackpolls.controllers;

import com.sahilten.quackpolls.domain.dto.user.UserDto;
import com.sahilten.quackpolls.domain.dto.user.UserResponseDto;
import com.sahilten.quackpolls.domain.entities.UserEntity;
import com.sahilten.quackpolls.domain.mappers.UserMapper;
import com.sahilten.quackpolls.security.user.QuackpollUserDetails;
import com.sahilten.quackpolls.services.JwtService;
import com.sahilten.quackpolls.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserMapper userMapper;
    private final UserService userService;
    private final JwtService jwtService;

    @GetMapping
    public ResponseEntity<UserResponseDto> getUser(@AuthenticationPrincipal QuackpollUserDetails userDetails,
                                                   @CookieValue(name = "access_token", required = false) String accessToken) {

        String email = userDetails.getUsername();

        UserEntity userEntity = userService.get(email);
        UserDto userDto = userMapper.toDto(userEntity);

        Date accessTokenExpiresAt = jwtService.extractExpiryFromToken(accessToken);
        UserResponseDto userResponseDto = new UserResponseDto(userDto, accessTokenExpiresAt);
        
        return ResponseEntity.ok(userResponseDto);
    }

}
