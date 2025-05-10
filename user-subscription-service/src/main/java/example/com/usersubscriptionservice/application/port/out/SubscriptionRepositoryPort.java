package example.com.usersubscriptionservice.application.port.out;


import example.com.usersubscriptionservice.domain.model.Subscription;
import example.com.usersubscriptionservice.domain.model.valueobject.SubscriptionService;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepositoryPort {
    Subscription save(Subscription subscription);
    List<Subscription> findAll();
    Optional<Subscription> findById(Long id);
    boolean existsById(Long id);
    void deleteById(Long id);
    List<Subscription> findAllByUserId(Long userId);

    boolean existsByUserIdAndService(Long userId, SubscriptionService subscriptionService);

}
