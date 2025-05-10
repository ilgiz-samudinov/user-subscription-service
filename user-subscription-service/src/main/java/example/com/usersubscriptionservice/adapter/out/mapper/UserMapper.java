package example.com.usersubscriptionservice.adapter.out.mapper;

import example.com.usersubscriptionservice.adapter.in.web.dto.UserRequest;
import example.com.usersubscriptionservice.adapter.in.web.dto.UserResponse;
import example.com.usersubscriptionservice.adapter.out.entity.UserEntity;
import example.com.usersubscriptionservice.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toDomain (UserEntity userEntity);

    UserEntity toEntity (User user);

    @Mapping(target = "id", ignore = true)
    void mergeUser(@MappingTarget User existing, User updated);

    UserResponse toResponse (User user);

    User toDomain(UserRequest userRequest);

}
