package example.com.usersubscriptionservice.adapter.in.web.dto;

import example.com.usersubscriptionservice.domain.model.valueobject.SubscriptionService;
import example.com.usersubscriptionservice.domain.model.valueobject.SubscriptionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class SubscriptionRequest {

//    @NotNull(message = "ID пользователя обязательно для заполнения")
//    @PositiveOrZero(message = "ID пользователя должен быть положительным числом")
//    private Long userId;

    @NotNull(message = "Необходимо выбрать сервис подписки")
    private SubscriptionService subscriptionService;

    @NotNull(message = "Необходимо указать тип подписки")
    private SubscriptionType subscriptionType;

    private Boolean autoRenew = false;

}
