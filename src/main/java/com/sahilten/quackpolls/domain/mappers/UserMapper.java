package com.sahilten.quackpolls.domain.mappers;

import com.sahilten.quackpolls.domain.dto.user.UserDto;
import com.sahilten.quackpolls.domain.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "user.id", ignore = true)
    UserDto toDto(UserEntity userEntity);
}
