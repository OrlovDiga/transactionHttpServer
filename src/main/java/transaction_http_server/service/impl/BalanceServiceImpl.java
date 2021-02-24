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

    public BalanceServiceImpl(BalanceRepo repo) {
        balanceRepo = repo;
    }

    public BalanceServiceImpl() {
        balanceRepo = new BalanceRepo();
    }

    @Override
    public synchronized Balance createBalance() {

        Balance balance = null;
        try {
            balance = (Balance) balanceRepo.create().clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return balance;
    }

    @Override
    public synchronized Balance findById(String id) {
        Balance foundBalance = balanceRepo.findById(id)
                .orElseThrow(() -> {throw new EntityNotFoundException(Balance.class, "id", id);});
        Balance cloneFoundBalance = null;
        try {
            cloneFoundBalance = (Balance) foundBalance.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return cloneFoundBalance;
    }

    @Override
    public synchronized void removeBalance(String balanceId) {
        balanceRepo.delete(balanceId);
    }

    @Override
    public synchronized Balance reduceBalance(String balanceId, long sum) {
        Balance foundBalance = findById(balanceId);
        long newMoneyAmount = foundBalance.getMoneyAmount() - sum;
        if (newMoneyAmount < 0) {
            throw new InsufficientFundsException(foundBalance.getId(), sum, foundBalance.getMoneyAmount());
        }
        foundBalance.setMoneyAmount(newMoneyAmount);
        Balance updatedBalance = null;
        try {
            updatedBalance = (Balance) balanceRepo.update(foundBalance).clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return updatedBalance;
    }

    @Override
    public synchronized Balance increaseBalance(String balanceId, long sum) {
        Balance foundBalance = findById(balanceId);
        foundBalance.setMoneyAmount(foundBalance.getMoneyAmount() + sum);
        Balance updatedBalance = null;
        try {
            updatedBalance = (Balance) balanceRepo.update(foundBalance).clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return updatedBalance;
    }
}