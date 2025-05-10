package example.com.usersubscriptionservice.domain.model.valueobject;

import java.math.BigDecimal;
import java.time.LocalDate;

public enum SubscriptionType {
    TRIAL(BigDecimal.ZERO, Currency.USD),
    BASIC(BigDecimal.valueOf(10), Currency.USD),
    PREMIUM(BigDecimal.valueOf(25), Currency.USD),
    ENTERPRISE(BigDecimal.valueOf(100), Currency.USD);

    private final BigDecimal price;
    private final Currency currency;

    SubscriptionType(BigDecimal price, Currency currency) {
        this.price = price;
        this.currency = currency;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public LocalDate calculateEndDate(LocalDate startDate) {
        return switch (this) {
            case TRIAL -> startDate.plusDays(7);
            case BASIC, PREMIUM -> startDate.plusMonths(1);
            case ENTERPRISE -> startDate.plusYears(1);
        };
    }
}

