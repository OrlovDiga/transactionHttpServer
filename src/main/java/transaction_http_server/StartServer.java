package transaction_http_server;

import com.sun.net.httpserver.HttpServer;
import transaction_http_server.handler.CustomHttpHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author Orlov Diga
 */
public class StartServer {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);
        server.createContext("/balance", new CustomHttpHandler());
        server.start();
    }
}
