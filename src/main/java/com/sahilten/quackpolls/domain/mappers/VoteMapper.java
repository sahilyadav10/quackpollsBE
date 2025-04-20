package com.sahilten.quackpolls.domain.mappers;

import com.sahilten.quackpolls.domain.dto.vote.VoteRequestDto;
import com.sahilten.quackpolls.domain.entities.VoteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VoteMapper {

    @Mapping(target = "user.id", ignore = true)
    VoteEntity toEntity(VoteRequestDto voteRequest);
}
