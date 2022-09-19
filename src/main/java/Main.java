import HttpServer.HttpTaskServer;
import HttpServer.KVServer;
import HttpServer.KVTaskClient;
import servise.*;
import tasks.*;

import java.io.IOException;
import java.net.URI;


public class Main {


    public static void main(String[] args) throws ManagerSaveException, IOException, InterruptedException {
        //Запускаем KVServer
        KVServer kvServer = new KVServer();
        kvServer.start();
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.loadServer();


    }
}