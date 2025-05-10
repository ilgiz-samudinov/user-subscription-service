package example.com.usersubscriptionservice.domain.model.valueobject;

public enum Currency {
    USD("USD", "$"),
    EUR("EUR", "€");

    private final String code;
    private final String symbol;

    Currency(String code, String symbol) {
        this.code = code;
        this.symbol = symbol;
    }

    public String getCode() {
        return code;
    }

    public String getSymbol() {
        return symbol;
    }
}
