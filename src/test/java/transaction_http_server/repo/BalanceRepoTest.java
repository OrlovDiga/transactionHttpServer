package transaction_http_server.repo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import transaction_http_server.domain.Balance;

import java.util.*;

/**
 * @author Orlov Diga
 */
public class BalanceRepoTest {

    private BalanceRepo repo;
    private List<Balance> balances;
    private static final int CREATED_BALANCE_COUNT = 100;
    private static final int TEST_COUNT = 100;

    @Before
    public void setUp() {
        repo = new BalanceRepo();
        balances = new ArrayList<>();
        for (int i = 0; i < TEST_COUNT; i++) {
            balances.add(repo.create());
        }
    }

    @Test
    public void findByIdSuccess() {
        for (int i = 0; i < TEST_COUNT; i++) {
            Balance balance = balances.get(randomIndex());
            Balance foundBalance = repo.findById(balance.getId()).get();
            Assert.assertEquals(balance, foundBalance);
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void findByIdError() {
        for (int i = 0; i < TEST_COUNT; i++) {
            repo.findById(UUID.randomUUID().toString()).get();
        }
    }

    @Test()
    public void deleteSuccess() {
        for (int i = 0; i < TEST_COUNT; i++) {
            Balance balance = balances.get(randomIndex());
            repo.delete(balance.getId());
            Assert.assertFalse(balance.isActive());
            Balance foundBalance = repo.findById(balance.getId()).orElse(null);
            Assert.assertNull(foundBalance);
        }
    }

    @Test
    public void createSuccess() {
        for (int i = 0; i < TEST_COUNT; i ++) {
            Balance balance = repo.create();
            Balance foundBalance = repo.findById(balance.getId()).get();
            Assert.assertEquals(balance, foundBalance);
        }
    }

    @Test
    public void update() {
        for (int i = 0; i< TEST_COUNT; i++) {
            Balance balance = balances.get(randomIndex());
            Balance foundBalance = repo.findById(balance.getId()).get();
            int randomAmountMoney = randomIndex();
            foundBalance.setMoneyAmount(randomAmountMoney);
            repo.update(foundBalance);
            Balance foundUpdatedBalance = repo.findById(foundBalance.getId()).get();
            Assert.assertEquals(randomAmountMoney, foundUpdatedBalance.getMoneyAmount());
        }
    }

    private int randomIndex() {
        return new Random().nextInt(CREATED_BALANCE_COUNT);
    }
}

