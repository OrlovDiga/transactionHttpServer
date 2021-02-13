package transaction_http_server.repo;

import transaction_http_server.domain.Balance;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class BalanceRepo {

    private Map<String, Balance> db = new ConcurrentHashMap<>();

    public Optional<Balance> findById(String id) {
        Balance balance = db.get(id);

        return (balance != null && balance.isActive()) ?
                Optional.of(balance) :
                Optional.empty();
    }

    public void delete(String id) {
        Balance balance = db.get(id);
        balance.setActive(false);
    }

    public Balance create() {
        Balance balance = Balance.create();
        db.put(balance.getId(), balance);

        return balance;
    }

    public Balance update(Balance balance) {
        if (balance.isActive() && db.get(balance) != null) {
            db.put(balance.getId(), balance);
        }

        return db.get(balance.getId());
    }
}