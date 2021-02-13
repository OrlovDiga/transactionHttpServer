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
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import static transaction_http_server.constant.StatusCode.*;

/**
 * @author Orlov Diga
 */

public class CustomHttpHandler implements HttpHandler {

    private static final Logger LOG = Logger.getLogger("CustomHttpHandler");
    private static final short URL_WORDS_COUNT = 3;

    private final BalanceService service;
    private final ObjectMapper mapper;

    public CustomHttpHandler() {
        mapper = new ObjectMapper();
        service = new BalanceServiceImpl();
    }

    public void handle(HttpExchange exchange) throws IOException {
        String action = checkAndGetActions(exchange);
        if (action == null) {
            return;
        }
        InputStream body = exchange.getRequestBody();

        Balance balance = null;
        if (!action.equals("create")) {
            balance = mapper.readValue(body, Balance.class);
        }

        final Balance finalBalance = balance;
        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                switch (exchange.getRequestMethod()) {
                    case "GET": {
                        if (action.equals("find")) {
                            LOG.info("find request");
                            Balance foundBalance = null;
                            try {
                                foundBalance = service.findById(finalBalance.getId());
                            } catch (EntityNotFoundException ex) {
                                handleResponse(exchange, getErrorJsonMsg(ex.getMessage()), BAD_REQUEST);
                            }
                            String json = mapper.writeValueAsString(foundBalance);
                            handleResponse(exchange, json, SUCCESS);
                        }
                    }
                    break;
                    case "POST": {
                        switch (action) {
                            case "create": {
                                LOG.info("create request");
                                Balance createdBalance = service.createBalance();
                                String json = mapper.writeValueAsString(createdBalance);
                                handleResponse(exchange, json, CREATED);
                            }
                            break;
                            case "increase": {
                                LOG.info("increase request");
                                Balance updatedBalance = null;

                                try {
                                    updatedBalance =
                                            service.increaseBalance(finalBalance.getId(), finalBalance.getMoneyAmount());
                                } catch (EntityNotFoundException ex) {
                                    handleResponse(exchange, getErrorJsonMsg(ex.getMessage()), BAD_REQUEST);
                                }

                                String json = mapper.writeValueAsString(updatedBalance);
                                handleResponse(exchange, json, SUCCESS);
                            }
                            break;
                            case "reduce": {
                                LOG.info("reduce request");
                                Balance updatedBalance = null;

                                try {
                                    updatedBalance =
                                            service.reduceBalance(finalBalance.getId(), finalBalance.getMoneyAmount());
                                } catch (EntityNotFoundException | InsufficientFundsException ex) {
                                    handleResponse(exchange, getErrorJsonMsg(ex.getMessage()), BAD_REQUEST);
                                }

                                String json = mapper.writeValueAsString(updatedBalance);
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
                    case "DELETE": {
                        LOG.info("delete request");
                        service.removeBalance(finalBalance.getId());
                        handleResponse(exchange, "", SUCCESS);
                    }
                }
            }
        }).start();
    }

    @SneakyThrows
    private String checkAndGetActions(final HttpExchange exchange) {
        //check content-type
        String contentType = exchange.getRequestHeaders().getFirst("Content-Type");
        boolean error = false;
        if (contentType == null || !contentType.equals("application/json")) {
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


        httpExchange.getResponseHeaders().set("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(code, body.length());
        outputStream.write(body.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private String getErrorJsonMsg(String msg) {
        return mapper.createObjectNode().put("error_msg", msg).toPrettyString();
    }
}
