package transaction_http_server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.SneakyThrows;
import transaction_http_server.domain.Balance;
import transaction_http_server.exception.EntityNotFoundException;
import transaction_http_server.exception.InsufficientFundsException;
import transaction_http_server.service.BalanceService;
import transaction_http_server.service.impl.BalanceServiceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

import static transaction_http_server.constant.NetworkConstant.*;
import static transaction_http_server.constant.StatusCode.*;

/**
 * @author Orlov Diga
 */

public class CustomHttpHandler implements HttpHandler {

    private static final Logger LOG = Logger.getLogger("CustomHttpHandler");
    private static final short URL_WORDS_COUNT = 3;
    private static final short THREAD_COUNT = 4;

    private final BalanceService service;
    private final ObjectMapper mapper;
    private ThreadPoolExecutor executor;

    public CustomHttpHandler() {
        mapper = new ObjectMapper();
        service = new BalanceServiceImpl();
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREAD_COUNT);
    }

    public CustomHttpHandler(BalanceService service) {
        mapper = new ObjectMapper();
        this.service = service;
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREAD_COUNT);
    }

    public void handle(HttpExchange exchange) throws IOException {
        String action = checkAndGetActions(exchange);
        if (action == null) {
            return;
        }
        InputStream body = exchange.getRequestBody();

        Balance balance = null;
        if (!action.equals(CREATE_PATH)) {
            balance = mapper.readValue(body, Balance.class);
        }

        final Balance finalBalance = balance;
        executor.execute(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                ObjectMapper threadMapper = new ObjectMapper();
                switch (exchange.getRequestMethod()) {
                    case GET_REQUEST: {
                        if (action.equals(FIND_PATH)) {
                            LOG.info("find request");
                            Balance foundBalance = null;
                            try {
                                foundBalance = service.findById(finalBalance.getId());
                            } catch (EntityNotFoundException ex) {
                                handleResponse(exchange, getErrorJsonMsg(ex.getMessage()), BAD_REQUEST);
                            }
                            String json = threadMapper.writeValueAsString(foundBalance);
                            handleResponse(exchange, json, SUCCESS);
                        }
                    }
                    break;
                    case POST_REQUEST: {
                        switch (action) {
                            case CREATE_PATH: {
                                LOG.info("create request");
                                Balance createdBalance = service.createBalance();
                                String json = threadMapper.writeValueAsString(createdBalance);
                                handleResponse(exchange, json, CREATED);
                            }
                            break;
                            case INCREASE_PATH: {
                                LOG.info("increase request");
                                Balance updatedBalance = null;
                                try {
                                    updatedBalance =
                                            service.increaseBalance(finalBalance.getId(), finalBalance.getMoneyAmount());
                                } catch (EntityNotFoundException ex) {
                                    handleResponse(exchange, getErrorJsonMsg(ex.getMessage()), BAD_REQUEST);
                                }
                                String json = threadMapper.writeValueAsString(updatedBalance);
                                handleResponse(exchange, json, SUCCESS);
                            }
                            break;
                            case REDUCE_PATH: {
                                LOG.info("reduce request");
                                Balance updatedBalance = null;
                                try {
                                    updatedBalance =
                                            service.reduceBalance(finalBalance.getId(), finalBalance.getMoneyAmount());
                                } catch (EntityNotFoundException | InsufficientFundsException ex) {
                                    handleResponse(exchange, getErrorJsonMsg(ex.getMessage()), BAD_REQUEST);
                                }
                                String json = threadMapper.writeValueAsString(updatedBalance);
                                handleResponse(exchange, json, SUCCESS);
                            }
                            break;
                            default: {
                                handleResponse(exchange, getErrorJsonMsg("Invalid url."), BAD_REQUEST);
                                System.out.println("error");
                            }
                        }
                    }
                    break;
                    case DELETE_REQUEST: {
                        if (DELETE_PATH.equals(action))
                        LOG.info("delete request");
                        service.removeBalance(finalBalance.getId());
                        handleResponse(exchange, "", SUCCESS);
                    }
                }
            }
        });
    }

    @SneakyThrows
    private String checkAndGetActions(final HttpExchange exchange) {
        //check content-type
        String contentType = exchange.getRequestHeaders().getFirst(CONTENT_TYPE);
        boolean error = false;
        if ((contentType == null || !contentType.equals(JSON_TYPE))
                && !exchange.getRequestURI().getPath().contains(CREATE_PATH)) {
            LOG.info("Invalid content type");
            handleResponse(exchange, getErrorJsonMsg("Invalid content type"), BAD_REQUEST);
            error =  true;
        }
        //check url
        String[] pathArr = exchange.getRequestURI().getPath().split("/");
        if (pathArr.length != URL_WORDS_COUNT) {
            LOG.info("Invalid url.");
            handleResponse(exchange, getErrorJsonMsg("Invalid url"), NOT_FOUND);
            error = true;
        }

        return error ? null : pathArr[2];
    }

    private static void handleResponse(final HttpExchange httpExchange,
                                       final String body,
                                       final int code)  throws  IOException {
        OutputStream outputStream = httpExchange.getResponseBody();

        httpExchange.getResponseHeaders().set(CONTENT_TYPE, JSON_TYPE);
        httpExchange.sendResponseHeaders(code, body.length());
        outputStream.write(body.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private String getErrorJsonMsg(String msg) {
        return mapper.createObjectNode().put("error_msg", msg).toPrettyString();
    }
}
