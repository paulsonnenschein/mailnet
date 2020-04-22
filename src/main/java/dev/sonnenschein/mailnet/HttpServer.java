package dev.sonnenschein.mailnet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.mail.internet.MimeMessage;
import java.util.List;

import static spark.Spark.*;

public class HttpServer implements Runnable {
    private final List<MimeMessage> messages;
    private final int port;

    public HttpServer(List<MimeMessage> messages, int port) {
        this.messages = messages;
        this.port = port;
    }


    @Override
    public void run() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(MimeMessage.class, new MessageSerializer())
                .setDateFormat("yyyy-MM-dd'T'HH:mm'Z'")
                .create();

        port(port);
        staticFiles.location("/frontend-build");
        get("/hello", (req, res) -> "Hello World");
        get("/messages", (req, res) -> {
            res.type("application/json");
            return messages;
        }, gson::toJson);
    }
}
