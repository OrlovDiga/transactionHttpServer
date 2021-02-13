package transaction_http_server.service.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import transaction_http_server.domain.Balance;
import transaction_http_server.exception.EntityNotFoundException;
import transaction_http_server.exception.InsufficientFundsException;
import transaction_http_server.repo.BalanceRepo;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

/**
 * @author Orlov Diga
 */
@RunWith(MockitoJUnitRunner.class)
public class BalanceServiceImplTest {

    private static final int TEST_COUNT = 100;
    private static final int CREATED_BALANCE_COUNT = 100;

    @Mock
    private BalanceRepo repo;

    @InjectMocks
    private BalanceServiceImpl service;
    private List<Balance> balances;

    @Before
    public void setUp() throws Exception {
        balances = new ArrayList<>();
        for (int i = 0; i < CREATED_BALANCE_COUNT; i++) {
            balances.add(Balance.create());
        }
    }

    @Test
    public void createBalance() {
        for (int i = 0; i < TEST_COUNT; i++) {
            Balance balance = balances.get(randomIndex());
            doReturn(balance).when(repo).create();
            Balance createdBalance = service.createBalance();
            Assert.assertEquals(balance, createdBalance);
        }
    }

    @Test
    public void findByIdSuccess() {
       for (int i = 0; i < 100; i++) {
           Balance balance = balances.get(randomIndex());
           doReturn(Optional.of(balance)).when(repo).findById(balance.getId());
           Balance foundBalance = service.findById(balance.getId());
           Assert.assertEquals(balance, foundBalance);
       }
    }

    @Test(expected = EntityNotFoundException.class)
    public void findByIdException() {
        doReturn(Optional.empty()).when(repo).findById(any());
        service.findById(UUID.randomUUID().toString());
    }

    @Test
    public void removeBalance() {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        doNothing()
                .when(repo)
                .delete(captor.capture());
        String syntaxBalanceId = UUID.randomUUID().toString();
        service.removeBalance(syntaxBalanceId);
        Assert.assertEquals(captor.getValue(), syntaxBalanceId);
    }

    @Test
    public void reduceBalance() {
        for (int i = 0; i < TEST_COUNT; i++) {
            Balance balance = balances.get(randomIndex());
            int money = random();
            int currentMoney = money + 1;
            int minusMoney = money;
            balance.setMoneyAmount(currentMoney);
            doReturn(Optional.of(balance)).when(repo).findById(balance.getId());
            doReturn(balance).when(repo).update(balance);
            Balance updatedBalance = service.reduceBalance(balance.getId(), minusMoney);
            Assert.assertEquals(currentMoney - minusMoney, updatedBalance.getMoneyAmount());
        }
    }

    @Test(expected = InsufficientFundsException.class)
    public void reduceBalanceException() {
        Balance balance = balances.get(randomIndex());
        int money = random();
        int currentMoney = money;
        int minusMoney = money + 1;
        balance.setMoneyAmount(currentMoney);
        doReturn(Optional.of(balance)).when(repo).findById(balance.getId());
        service.reduceBalance(balance.getId(), minusMoney);
    }

    @Test
    public void increaseBalance() {
        for (int i = 0; i < TEST_COUNT; i++) {
            Balance balance = balances.get(randomIndex());
            int currentMoney = random();
            int plusMoney = random();
            balance.setMoneyAmount(currentMoney);
            doReturn(Optional.of(balance)).when(repo).findById(balance.getId());
            doReturn(balance).when(repo).update(balance);
            Balance updatedBalance = service.increaseBalance(balance.getId(), plusMoney);
            Assert.assertEquals(currentMoney + plusMoney, updatedBalance.getMoneyAmount());
        }
    }

    private int randomIndex() {
        return new Random().nextInt(CREATED_BALANCE_COUNT);
    }

    private int random() {
        return new Random().nextInt(1000) + 100;
    }
}
