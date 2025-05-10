package example.com.usersubscriptionservice.adapter.out.persistence;

import example.com.usersubscriptionservice.adapter.out.entity.SubscriptionEntity;
import example.com.usersubscriptionservice.adapter.out.mapper.SubscriptionMapper;
import example.com.usersubscriptionservice.adapter.out.repository.JpaSubscriptionRepository;
import example.com.usersubscriptionservice.application.port.out.SubscriptionRepositoryPort;

import example.com.usersubscriptionservice.domain.model.Subscription;
import example.com.usersubscriptionservice.domain.model.valueobject.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
@Component
@RequiredArgsConstructor
public class SubscriptionPersistenceAdapter implements SubscriptionRepositoryPort {
    private final JpaSubscriptionRepository jpaSubscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;

    @Override
    public Subscription save(Subscription subscription) {
        SubscriptionEntity subscriptionEntity = subscriptionMapper.toEntity(subscription);
        SubscriptionEntity saved = jpaSubscriptionRepository.save(subscriptionEntity);
        return subscriptionMapper.toDomain(saved);
    }

    @Override
    public List<Subscription> findAll() {
        return jpaSubscriptionRepository.findAll().stream().map(subscriptionMapper::toDomain).toList();
    }

    @Override
    public Optional<Subscription> findById(Long id) {
        return jpaSubscriptionRepository.findById(id).map(subscriptionMapper::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaSubscriptionRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        jpaSubscriptionRepository.deleteById(id);
    }

    @Override
    public List<Subscription> findAllByUserId(Long userId) {
        return jpaSubscriptionRepository.findAllByUserEntity_Id(userId).stream().map(subscriptionMapper::toDomain).toList();
    }


    @Override
    public boolean existsByUserIdAndService(Long userId, SubscriptionService subscriptionService) {
        return jpaSubscriptionRepository.existsByUserEntity_IdAndSubscriptionService(userId, subscriptionService);
    }
}

