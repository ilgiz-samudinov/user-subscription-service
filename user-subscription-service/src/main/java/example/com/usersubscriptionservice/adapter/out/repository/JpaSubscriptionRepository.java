package example.com.usersubscriptionservice.adapter.out.repository;

import example.com.usersubscriptionservice.adapter.out.entity.SubscriptionEntity;

import example.com.usersubscriptionservice.application.port.out.TopSubscriptionProjection;

import example.com.usersubscriptionservice.domain.model.valueobject.SubscriptionService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaSubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {
    List<SubscriptionEntity>  findAllByUserEntity_Id(Long userId);
    boolean existsByUserEntity_IdAndSubscriptionService(Long userId, SubscriptionService subscriptionService);


    @Query("""
    SELECT s.subscriptionService AS service, COUNT(s.subscriptionService) AS count
    FROM SubscriptionEntity s
    GROUP BY s.subscriptionService
    ORDER BY count DESC
    """)
    List<TopSubscriptionProjection> findTop3SubscriptionsService(Pageable pageable);

}
