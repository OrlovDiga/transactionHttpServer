package transaction_http_server;

import com.sun.net.httpserver.HttpServer;
import transaction_http_server.handler.CustomHttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Properties;

import static transaction_http_server.constant.NetworkConstant.ROOT_PATH;

/**
 * @author Orlov Diga
 */
public class StartServer {

    public static void main(String[] args) throws IOException {
        int port = 8080;
        String hostIp = "localhost";
        try (InputStream input = StartServer.class.getClassLoader().getResourceAsStream("config.properties")) {

            Properties prop = new Properties();

            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }

            //load a properties file from class path, inside static method
            prop.load(input);

            //get the property value and print it out
            port = Integer.parseInt(prop.getProperty("app.port"));
            hostIp = prop.getProperty("app.host");

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        HttpServer server = HttpServer.create(new InetSocketAddress(hostIp, port), 0);
        server.createContext("/" + ROOT_PATH, new CustomHttpHandler());
        server.start();
    }
}
