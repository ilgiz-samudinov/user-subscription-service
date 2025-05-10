package example.com.usersubscriptionservice.application.service;

import example.com.usersubscriptionservice.adapter.in.web.dto.TopSubscriptionDto;
import example.com.usersubscriptionservice.adapter.out.repository.JpaSubscriptionRepository;
import example.com.usersubscriptionservice.application.exception.NotFoundException;
import example.com.usersubscriptionservice.application.exception.ValidationException;
import example.com.usersubscriptionservice.application.port.in.SubscriptionUseCase;
import example.com.usersubscriptionservice.application.port.out.SubscriptionRepositoryPort;
import example.com.usersubscriptionservice.application.port.out.TopSubscriptionProjection;
import example.com.usersubscriptionservice.domain.model.Subscription;
import example.com.usersubscriptionservice.domain.model.valueobject.Status;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService implements SubscriptionUseCase {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionService.class);

    private final SubscriptionRepositoryPort subscriptionRepository;
    private final JpaSubscriptionRepository jpaSubscriptionRepository;

    @Transactional
    @Override
    public Subscription createSubscription(Subscription subscription) {
        logger.info("Создание подписки для пользователя ID: {}", subscription.getUser().getId());

        if (subscriptionRepository.existsByUserIdAndService(subscription.getUser().getId(), subscription.getSubscriptionService())) {
            logger.warn("Подписка уже существует для пользователя ID: {}", subscription.getUser().getId());
            throw new ValidationException("Подписка для этого пользователя уже существует");
        }

        subscription.setStatus(Status.ACTIVE);
        subscription.setStartDate(LocalDate.now());
        subscription.setEndDate(subscription.getSubscriptionType().calculateEndDate(subscription.getStartDate()));
        subscription.setPrice(subscription.getSubscriptionType().getPrice());
        subscription.setCurrency(subscription.getSubscriptionType().getCurrency());

        Subscription saved = subscriptionRepository.save(subscription);
        logger.debug("Подписка создана: {}", saved);
        return saved;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Subscription> getAllSubscriptions() {
        logger.info("Получение всех подписок");
        return subscriptionRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Subscription> getAllUserSubscriptions(Long id) {
        logger.info("Получение подписок пользователя ID: {}", id);
        return subscriptionRepository.findAllByUserId(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Subscription getSubscription(Long id) {
        logger.info("Получение подписки ID: {}", id);
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Подписка с ID {} не найдена", id);
                    return new NotFoundException("Подписка с указанным ID не найдена");
                });
    }

    @Transactional
    @Override
    public void deleteSubscription(Long id) {
        logger.info("Удаление подписки с ID: {}", id);
        if (!subscriptionRepository.existsById(id)) {
            logger.warn("Попытка удалить несуществующую подписку с ID: {}", id);
            throw new NotFoundException("Подписка с указанным ID не найдена");
        }
        subscriptionRepository.deleteById(id);
        logger.debug("Подписка с ID {} успешно удалена", id);
    }

    @Transactional
    @Override
    public void editAutoRenew(Long id, Boolean autoRenew) {
        logger.info("Изменение автопродления для подписки ID: {} на {}", id, autoRenew);

        if (autoRenew == null) {
            logger.error("Значение autoRenew не может быть null для подписки с ID: {}", id);
            throw new ValidationException("Значение для автопродления не может быть null");
        }

        Subscription subscription = getSubscription(id);

        if (!subscription.isActive()) {
            logger.warn("Попытка изменить автопродление для неактивной подписки с ID: {}", id);
            throw new ValidationException("Автопродление можно изменить только для активных подписок");
        }

        subscription.setAutoRenew(autoRenew);
        subscriptionRepository.save(subscription);
        logger.debug("Автопродление для подписки с ID: {} успешно изменено", id);
    }

    @Transactional
    @Override
    public void cancelSubscription(Long id) {
        logger.info("Отмена подписки с ID: {}", id);
        Subscription subscription = getSubscription(id);
        subscription.cancel();
        subscriptionRepository.save(subscription);
        logger.debug("Подписка с ID {} отменена", id);
    }

    @Transactional
    @Override
    public void pauseSubscription(Long id) {
        logger.info("Приостановка подписки с ID: {}", id);
        Subscription subscription = getSubscription(id);
        subscription.pause();
        subscriptionRepository.save(subscription);
        logger.debug("Подписка с ID {} приостановлена", id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TopSubscriptionDto> getTopSubscriptions() {
        logger.info("Получение топ-3 популярных подписок");
        List<TopSubscriptionProjection> projections = jpaSubscriptionRepository
                .findTop3SubscriptionsService(PageRequest.of(0, 3));

        int[] counter = {1};

        List<TopSubscriptionDto> top = projections.stream()
                .map(p -> new TopSubscriptionDto(
                        counter[0]++,
                        p.getService().toString(),
                        p.getCount()
                ))
                .toList();

        logger.debug("Топ подписок: {}", top);
        return top;
    }
}
