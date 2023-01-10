
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
import java.util.Date;
public class main {
    public static Properties config = ConfigLoader.load("app.config");
    public static String port = ConfigLoader.get(config, "port");
    public static boolean mt = false;
    //If you're building a custom version, consider changing these. They show up on /espresso-version.
    public static String VER = "2.0.0";
    public static String BUILDSTR = "Standard build";
    //
    public static long UPTIME;
    public static void print(Object msg) {
        System.out.println(msg);
    }
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(Integer.parseInt(port)), 0);
        UPTIME = System.currentTimeMillis();
        System.out.println("Started on port " + port + " at " + new Date(UPTIME));
        server.createContext("/", new Handler());
        server.setExecutor(null);
        server.start();
        
        boolean closing = false;
        System.out.println("For a full list of commands, see DOCS.html.");
        while (!closing) {
            Scanner sc = new Scanner(System.in);
            String in = sc.nextLine();
            switch (in) {
                case "exit": {closing = true; break;}
                case "rconfig": {main.config = ConfigLoader.load("app.config"); main.print("Reloaded config"); break;}
                case "entermt": {mt = true; main.print("Entered maintenance mode"); break;}
                case "exitmt": {mt = false; main.print("Exited maintenance mode"); break;}
            }
        }
        server.stop(0);
        System.out.println("Exited");
    }
}