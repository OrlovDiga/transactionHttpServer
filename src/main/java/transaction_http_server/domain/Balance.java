package transaction_http_server.domain;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public final class Balance {
    private String id;
    private long moneyAmount;
    private boolean isActive;

    private Balance() {
        this.id = UUID.randomUUID().toString();
        this.isActive = true;
    }

    public static Balance create() {
        return new Balance();
    }
}