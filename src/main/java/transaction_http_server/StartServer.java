package transaction_http_server;

import com.sun.net.httpserver.HttpServer;
import transaction_http_server.handler.CustomHttpHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

import static transaction_http_server.constant.NetworkConstant.ROOT_PATH;

/**
 * @author Orlov Diga
 */
public class StartServer {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);
        server.createContext("/" + ROOT_PATH, new CustomHttpHandler());
        server.start();
    }
}
