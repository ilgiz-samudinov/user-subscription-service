package example.com.usersubscriptionservice.domain.model;

import example.com.usersubscriptionservice.domain.model.valueobject.Currency;
import example.com.usersubscriptionservice.domain.model.valueobject.Status;
import example.com.usersubscriptionservice.domain.model.valueobject.SubscriptionService;
import example.com.usersubscriptionservice.domain.model.valueobject.SubscriptionType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;


@Getter
@Setter
public class Subscription {
    private Long id;
    private User user;
    private SubscriptionService subscriptionService;
    private SubscriptionType subscriptionType;
    private LocalDate  startDate;
    private LocalDate  endDate;
    private Status status;
    private Boolean autoRenew;

    private BigDecimal price;
    private Currency currency;


    private Instant createdAt;
    private Instant updatedAt;


    public void cancel() {
        this.status = Status.CANCELLED;
    }

    public void pause() {
        this.status = Status.PAUSED;
    }

    public boolean isActive() {
        return this.status == Status.ACTIVE;
    }

}
