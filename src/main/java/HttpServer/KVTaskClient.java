package HttpServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private URI uri;
    private HttpClient client;
    private String apiToken;

    public KVTaskClient(URI uri) throws IOException, InterruptedException {
        this.uri = uri;
        client = HttpClient.newHttpClient();
        apiToken = register();
    }

    private String register() throws IOException, InterruptedException {
        URI uri1 = URI.create(uri + "/register/");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri1)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        URI uri1 = URI.create(uri + "/save/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri1)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        client.send(request, handler);
    }

    public String load(String key) throws IOException, InterruptedException {
        URI uri1 = URI.create(uri + "/load/" + key+ "?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri1)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        return client.send(request, handler).body();
    }
}