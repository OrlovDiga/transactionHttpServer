package transaction_http_server.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import transaction_http_server.domain.Balance;
import transaction_http_server.service.impl.BalanceServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.*;
import static transaction_http_server.constant.NetworkConstant.*;

/**
 * @author Orlov Diga
 */
//@RunWith(MockitoJUnitRunner.class)
public class CustomHttpHandlerTest {

 /*   private static final String ROOT = "/" + ROOT_PATH;
    private static final int TEST_COUNT = 100;
    private static final int CREATED_BALANCE_COUNT = 100;

    @Mock
    private HttpExchange exchange;

    @Mock
    private BalanceServiceImpl service;

    @InjectMocks
    private CustomHttpHandler handler;

    private List<Balance> balances;

    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        balances = new ArrayList<>();
        for (int i = 0; i < CREATED_BALANCE_COUNT; i++) {
            balances.add(Balance.create());
        }
    }

    @Test
    public void handleFindRequest() throws JsonProcessingException {
        Balance balance = balances.get(randomIndex());
        String requestJson = mapper.writeValueAsString(balance);

        when(exchange.getRequestHeaders().getFirst(CONTENT_TYPE)).thenReturn(JSON_TYPE);
        //doReturn(JSON_TYPE).when(exchange).getRequestHeaders().getFirst(CONTENT_TYPE);
        doReturn(ROOT + "/" + FIND_PATH).when(exchange).getRequestURI().getPath();
        doReturn(GET_REQUEST).when(exchange).getRequestMethod();
        doReturn(requestJson).when(exchange).getRequestBody();
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        doNothing()
                .when(service)
                .findById(captor.capture());
        Assert.assertEquals(balance.getId(), captor.getValue());
    }

    @Test
    public void handleCreateRequest() {
        doReturn(JSON_TYPE).when(exchange).getRequestHeaders().getFirst(CONTENT_TYPE);
        doReturn(ROOT + "/" + CREATE_PATH).when(exchange).getRequestURI().getPath();
        doReturn(POST_REQUEST).when(exchange).getRequestMethod();
    }

    @Test
    public void handleDeleteRequest() {
        doReturn(JSON_TYPE).when(exchange).getRequestHeaders().getFirst(CONTENT_TYPE);
        doReturn(ROOT + "/" + DELETE_PATH).when(exchange).getRequestURI().getPath();
        doReturn(DELETE_REQUEST).when(exchange).getRequestMethod();
    }

    @Test
    public void handleReduceRequest() {
        doReturn(JSON_TYPE).when(exchange).getRequestHeaders().getFirst(CONTENT_TYPE);
        doReturn(ROOT + "/" + REDUCE_PATH).when(exchange).getRequestURI().getPath();
        doReturn(POST_REQUEST).when(exchange).getRequestMethod();
    }

    @Test
    public void handleIncreaseRequest() {
        doReturn(JSON_TYPE).when(exchange).getRequestHeaders().getFirst(CONTENT_TYPE);
        doReturn(ROOT + "/" + INCREASE_PATH).when(exchange).getRequestURI().getPath();
        doReturn(POST_REQUEST).when(exchange).getRequestMethod();
    }

    private int randomIndex() {
        return new Random().nextInt(CREATED_BALANCE_COUNT);
    }

    private int random() {
        return new Random().nextInt(1000) + 100;
    }*/
}
