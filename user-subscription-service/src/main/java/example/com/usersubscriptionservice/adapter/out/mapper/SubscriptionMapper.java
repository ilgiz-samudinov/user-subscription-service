package example.com.usersubscriptionservice.adapter.out.mapper;

import example.com.usersubscriptionservice.adapter.in.web.dto.SubscriptionRequest;
import example.com.usersubscriptionservice.adapter.in.web.dto.SubscriptionResponse;
import example.com.usersubscriptionservice.adapter.out.entity.SubscriptionEntity;
import example.com.usersubscriptionservice.application.port.in.UserUseCase;
import example.com.usersubscriptionservice.domain.model.Subscription;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    @Mapping(target = "user", source = "userEntity")
    Subscription toDomain(SubscriptionEntity subscriptionEntity);

    @Mapping(target = "userEntity", source = "user")
    SubscriptionEntity toEntity(Subscription subscription);

    @Mapping(target = "id", ignore = true)
    void merge(@MappingTarget Subscription existing, Subscription updated);

    @Mapping(
            target = "user",
            expression = "java(userUseCase.getUser(subscriptionRequest.getUserId()))"
    )
    Subscription toDomain(SubscriptionRequest subscriptionRequest, @Context UserUseCase userUseCase);

    @Mapping(target = "user", source = "user")
    SubscriptionResponse toResponse(Subscription subscription);
}
