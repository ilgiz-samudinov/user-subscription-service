package example.com.usersubscriptionservice.adapter.in.web;

import example.com.usersubscriptionservice.adapter.in.web.dto.SubscriptionRequest;
import example.com.usersubscriptionservice.adapter.in.web.dto.SubscriptionResponse;
import example.com.usersubscriptionservice.adapter.in.web.dto.TopSubscriptionDto;
import example.com.usersubscriptionservice.adapter.out.mapper.SubscriptionMapper;
import example.com.usersubscriptionservice.application.port.in.SubscriptionUseCase;
import example.com.usersubscriptionservice.application.port.in.UserUseCase;
import example.com.usersubscriptionservice.domain.model.Subscription;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SubscriptionController {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionController.class);

    private final SubscriptionUseCase subscriptionUseCase;
    private final SubscriptionMapper subscriptionMapper;
    private final UserUseCase userUseCase;

    @PostMapping("/users/{userId}/subscriptions")
    public ResponseEntity<SubscriptionResponse> createSubscription(
            @PathVariable Long userId,
            @Valid @RequestBody SubscriptionRequest subscriptionRequest) {

        logger.info("Запрос на создание подписки для пользователя с ID: {}", userId);

        Subscription subscription = subscriptionMapper.toDomain(subscriptionRequest, userUseCase, userId);
        Subscription createdSubscription = subscriptionUseCase.createSubscription(subscription);

        logger.debug("Подписка успешно создана для пользователя с ID: {}", userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subscriptionMapper.toResponse(createdSubscription));
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<List<SubscriptionResponse>> getAllSubscriptions() {
        logger.info("Запрос на получение всех подписок");
        List<SubscriptionResponse> response = subscriptionUseCase.getAllSubscriptions().stream()
                .map(subscriptionMapper::toResponse)
                .toList();
        logger.debug("Получено {} подписок", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{userId}/subscriptions")
    public ResponseEntity<List<SubscriptionResponse>> getAllUserSubscriptions(@PathVariable Long userId) {
        logger.info("Запрос на получение всех подписок для пользователя с ID: {}", userId);
        List<SubscriptionResponse> response = subscriptionUseCase.getAllUserSubscriptions(userId).stream()
                .map(subscriptionMapper::toResponse)
                .toList();
        logger.debug("Получено {} подписок для пользователя с ID: {}", response.size(), userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{userId}/subscriptions/{id}")
    public ResponseEntity<SubscriptionResponse> getSubscription(@PathVariable Long userId,
                                                                @PathVariable Long id) {
        logger.info("Запрос на получение подписки с ID: {} для пользователя с ID: {}", id, userId);
        SubscriptionResponse response = subscriptionMapper.toResponse(subscriptionUseCase.getSubscription(id));
        logger.debug("Подписка с ID: {} успешно получена для пользователя с ID: {}", id, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/users/{userId}/subscriptions/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long userId,
                                                   @PathVariable Long id) {
        logger.info("Запрос на удаление подписки с ID: {} для пользователя с ID: {}", id, userId);
        subscriptionUseCase.deleteSubscription(id);
        logger.debug("Подписка с ID: {} успешно удалена для пользователя с ID: {}", id, userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/users/{userId}/subscriptions/{id}/auto-renew")
    public ResponseEntity<Void> editAutoRenew(@PathVariable Long userId,
                                              @PathVariable Long id,
                                              @RequestParam Boolean autoRenew) {
        logger.info("Запрос на изменение автообновления подписки с ID: {} для пользователя с ID: {}", id, userId);
        subscriptionUseCase.editAutoRenew(id, autoRenew);
        logger.debug("Автообновление для подписки с ID: {} для пользователя с ID: {} изменено на {}", id, userId, autoRenew);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/users/{userId}/subscriptions/{id}/cancel")
    public ResponseEntity<Void> cancelSubscription(@PathVariable Long userId,
                                                   @PathVariable Long id) {
        logger.info("Запрос на отмену подписки с ID: {} для пользователя с ID: {}", id, userId);
        subscriptionUseCase.cancelSubscription(id);
        logger.debug("Подписка с ID: {} для пользователя с ID: {} отменена", id, userId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/users/{userId}/subscriptions/{id}/pause")
    public ResponseEntity<Void> pausedSubscription(@PathVariable Long userId,
                                                   @PathVariable Long id) {
        logger.info("Запрос на приостановку подписки с ID: {} для пользователя с ID: {}", id, userId);
        subscriptionUseCase.pauseSubscription(id);
        logger.debug("Подписка с ID: {} для пользователя с ID: {} приостановлена", id, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/subscriptions/top")
    public ResponseEntity<List<TopSubscriptionDto>> getTopSubscriptions() {
        logger.info("Запрос на получение топ-3 подписок");
        List<TopSubscriptionDto> topList = subscriptionUseCase.getTopSubscriptions();
        logger.debug("Получены топ-3 подписки");
        return ResponseEntity.ok(topList);
    }
}
