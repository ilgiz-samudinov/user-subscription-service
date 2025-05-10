package example.com.usersubscriptionservice.adapter.in.web.dto;

import example.com.usersubscriptionservice.domain.model.User;
import example.com.usersubscriptionservice.domain.model.valueobject.Currency;
import example.com.usersubscriptionservice.domain.model.valueobject.Status;
import example.com.usersubscriptionservice.domain.model.valueobject.SubscriptionService;
import example.com.usersubscriptionservice.domain.model.valueobject.SubscriptionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Data
public class SubscriptionResponse {
    private Long id;
    private User user;
    private SubscriptionService subscriptionService;
    private SubscriptionType subscriptionType;
    private LocalDate startDate;
    private LocalDate  endDate;
    private Status status;
    private Boolean autoRenew;
    private BigDecimal price;
    private Currency currency;
    private Instant createdAt;
    private Instant updatedAt;
}
