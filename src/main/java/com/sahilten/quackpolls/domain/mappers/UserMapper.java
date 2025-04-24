package com.sahilten.quackpolls.domain.mappers;

import com.sahilten.quackpolls.domain.dto.user.UserDto;
import com.sahilten.quackpolls.domain.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserDto toDto(UserEntity userEntity);
}
