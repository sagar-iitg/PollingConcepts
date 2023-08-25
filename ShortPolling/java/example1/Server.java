import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;

public class Server {
    public static void main(String[] args) throws Exception {
        // Create a new HTTP server instance
        HttpServer server = HttpServer.create(new InetSocketAddress(8001), 0);

        // Create a context for the root path "/"
        server.createContext("/app", new MyHandler());

        // Start the server
        server.start();

        System.out.println("Server started on port 8001");
    }

    // Custom handler to respond to requests
    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Set the response headers
            exchange.getResponseHeaders().add("Content-Type", "text/html");
            exchange.sendResponseHeaders(200, 0);

            // Get the output stream to write the response
            OutputStream os = exchange.getResponseBody();

           // Read the HTML content from the external file
            File htmlFile = new File("index.html");
            InputStream fileStream = Files.newInputStream(htmlFile.toPath());

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileStream.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }

            fileStream.close();

            // Close the output stream
            os.close();
        }
    }
}
