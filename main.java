
/**
 * Main class
 * 
 * @author jweled
 * @version 1.0.0
 */
import java.net.*;
import com.sun.net.httpserver.*;
import java.util.Scanner;
import java.util.Properties;
public class main {
    public static Properties config = ConfigLoader.load("app.config");
    public static String port = ConfigLoader.get(config, "port");
    public static void print(Object msg) {
        System.out.println(msg);
    }
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(Integer.parseInt(port)), 0);
        System.out.println("Started on port " + port);
        server.createContext("/", new Handler());
        server.setExecutor(null);
        server.start();
        
        boolean closing = false;
        System.out.println("For a full list of commands, see DOCS.html.");
        while (!closing) {
            Scanner sc = new Scanner(System.in);
            String in = sc.nextLine();
            switch (in) {
                case "exit": closing = true;
            }
        }
        server.stop(0);
        System.out.println("Exited");
    }
}