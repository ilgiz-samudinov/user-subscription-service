package example.com.usersubscriptionservice.application.port.out;

import example.com.usersubscriptionservice.domain.model.valueobject.SubscriptionService;

public interface TopSubscriptionProjection {
    SubscriptionService getService();
    Long getCount();
}
