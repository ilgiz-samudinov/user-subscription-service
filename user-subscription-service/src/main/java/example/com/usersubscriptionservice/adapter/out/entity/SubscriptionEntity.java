package example.com.usersubscriptionservice.adapter.out.entity;

import example.com.usersubscriptionservice.domain.model.valueobject.Currency;
import example.com.usersubscriptionservice.domain.model.valueobject.Status;
import example.com.usersubscriptionservice.domain.model.valueobject.SubscriptionService;
import example.com.usersubscriptionservice.domain.model.valueobject.SubscriptionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "subscriptions")
public class SubscriptionEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionService subscriptionService;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionType subscriptionType;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate  endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    private Boolean autoRenew;

    @Column(nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    protected void onCreate(){
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate(){
        updatedAt = Instant.now();
    }
}
