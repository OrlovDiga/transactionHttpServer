package transaction_http_server.service.impl;

import transaction_http_server.domain.Balance;
import transaction_http_server.exception.EntityNotFoundException;
import transaction_http_server.exception.InsufficientFundsException;
import transaction_http_server.repo.BalanceRepo;
import transaction_http_server.service.BalanceService;

/**
 * @author Orlov Diga
 */
public class BalanceServiceImpl implements BalanceService {

    private final BalanceRepo balanceRepo;

    public BalanceServiceImpl() {
        balanceRepo = new BalanceRepo();
    }

    @Override
    public Balance createBalance() {
        return balanceRepo.create();
    }

    @Override
    public Balance findById(String id) {
        return balanceRepo.findById(id)
                .orElseThrow(() -> {throw new EntityNotFoundException(Balance.class, "id", id);});
    }

    @Override
    public void removeBalance(String balanceId) {
        balanceRepo.delete(balanceId);
    }

    @Override
    public Balance reduceBalance(String balanceId, long sum) {
        Balance balance = findById(balanceId);
        long newMoneyAmount = balance.getMoneyAmount() - sum;
        if (newMoneyAmount < 0) {
            throw new InsufficientFundsException(balance.getId(), sum, balance.getMoneyAmount());
        }
        balance.setMoneyAmount(newMoneyAmount);
        return balanceRepo.update(balance);
    }

    @Override
    public Balance increaseBalance(String balanceId, long sum) {
        Balance balance = findById(balanceId);
        balance.setMoneyAmount(balance.getMoneyAmount() + sum);
        return balanceRepo.update(balance);
    }
}