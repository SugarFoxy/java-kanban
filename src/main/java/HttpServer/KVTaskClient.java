package HttpServer;

import servise.ManagerSaveException;

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

    private String register()  {
        URI uri1 = URI.create(uri + "/register/");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri1)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException(e);
        }
    }

    public void put(String key, String json){
        URI uri1 = URI.create(uri + "/save/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri1)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException(e);
        }
    }

    public String load(String key)  {
        URI uri1 = URI.create(uri + "/load/" + key+ "?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri1)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            return client.send(request, handler).body();
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException(e);
        }
    }
}