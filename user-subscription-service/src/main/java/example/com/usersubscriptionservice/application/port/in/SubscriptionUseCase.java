package example.com.usersubscriptionservice.application.port.in;

import example.com.usersubscriptionservice.adapter.in.web.dto.TopSubscriptionDto;
import example.com.usersubscriptionservice.domain.model.Subscription;
import example.com.usersubscriptionservice.domain.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SubscriptionUseCase {
    Subscription createSubscription(Subscription subscription);
    List<Subscription> getAllSubscriptions();

    List<Subscription> getAllUserSubscriptions(Long id);

    Subscription getSubscription(Long id);
    void deleteSubscription(Long id);
    void editAutoRenew(Long id, Boolean autoRenew);
    void cancelSubscription(Long id);
    void pauseSubscription(Long id);

    public List<TopSubscriptionDto> getTopSubscriptions();
}

