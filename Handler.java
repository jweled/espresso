
/**
 * Server request handler
 * 
 * @author jweled
 * @version 1.0.0
 */
import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
public class Handler implements HttpHandler {
    private String dir = ConfigLoader.get(main.config, "dir");
    private String getExt(String p) {
        int i = p.lastIndexOf('.');
        if (i > 0) {
            return p.substring(i+1);
        } else {
            return "";
        }
    }
    
    @Override
    public void handle(HttpExchange he) throws IOException {
        String realpath = dir + he.getRequestURI().getPath().replace("/", "\\");
        File file = new File(realpath);
        byte[] res = new byte[0];
        if (file.exists()) {
            InputStream reader = new FileInputStream(realpath);
            ByteArrayOutputStream outb = new ByteArrayOutputStream();
            main.print("\nReading " + file.length() + "b");
            //get mime type
            String gext = getExt(realpath);
            String enc = "";
            if (FileTypes.fileMap.containsKey(gext)) {
                enc = FileTypes.fileMap.get(gext);
            } else {
                enc = "application/octet-stream";
            }
            main.print(enc + " MIME type selected");
            //read
            if (gext.equals("esp") && !ConfigLoader.get(main.config, "esp_enabled").equals("false")) {
                main.print("Parsing ESP");
                String toParse = new String();
                int i = 0;
                while ((i = reader.read()) != -1) {
                    toParse += (char)(i);
                }
                String parsed = ESP.parse(toParse, Boolean.parseBoolean(ConfigLoader.get(main.config, "verbose")));
                outb.write(parsed.getBytes());
            } else {
                int i = 0;
                while ((i = reader.read()) != -1) {
                    outb.write(i);
                }
            }
            reader.close();
            res = outb.toByteArray();
            he.getResponseHeaders().set("Content-Type", enc);
            he.sendResponseHeaders(200, outb.size());
        } else {
            String msg = "404 Not Found";
            res = msg.getBytes();
            he.sendResponseHeaders(404, msg.length());
        }
        OutputStream out = he.getResponseBody();
        out.write(res);
        out.close();
    }
}
