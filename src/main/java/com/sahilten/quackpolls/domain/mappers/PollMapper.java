package com.sahilten.quackpolls.domain.mappers;

import com.sahilten.quackpolls.domain.dto.poll.CreatePollRequest;
import com.sahilten.quackpolls.domain.dto.poll.PollDto;
import com.sahilten.quackpolls.domain.dto.user.UserDto;
import com.sahilten.quackpolls.domain.entities.PollEntity;
import com.sahilten.quackpolls.domain.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


/**
 * PollMapper handles mapping between PollDto, PollEntity, and CreatePollRequest.
 * It simplifies repetitive boilerplate mapping logic, especially when dealing with renamed fields or nested objects.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PollMapper {
    /**
     * Maps PollDto to PollEntity.
     * Automatically maps all fields with the same name.
     * Manually maps userId → user.id (nested structure).
     */
    @Mapping(target = "user.id", ignore = true)
    // DTO userId is mapped to entity's user.id
    PollEntity toEntity(CreatePollRequest createPollRequest);

    /**
     * Maps PollEntity to PollDto.
     * Automatically maps all fields with the same name.
     * Manually maps user.id → userId.
     */
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "creator", source = "user")
    // Nested entity field user.id is flattened to userId in DTO
    PollDto toDto(PollEntity entity);


    /**
     * Maps UserEntity to CreatorUserDto.
     * This method is used implicitly by MapStruct when mapping PollEntity → PollDto,
     * because PollEntity.user is mapped to PollDto.creator.
     * No manual call needed — MapStruct wires it automatically.
     */
    UserDto toCreatorUserDto(UserEntity user);
}
