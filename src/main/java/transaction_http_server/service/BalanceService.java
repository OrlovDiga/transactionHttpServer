package transaction_http_server.service;

import transaction_http_server.domain.Balance;

/**
 * @author Orlov Diga
 */
public interface BalanceService {

    Balance findById(String id);

    Balance createBalance();

    void removeBalance(String balanceId);

    Balance reduceBalance(String balanceId, long sum);

    Balance increaseBalance(String balanceId, long sum);
}