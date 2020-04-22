package dev.sonnenschein.mailnet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.port;

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
        get("/hello", (req, res) -> "Hello World");
        get("/messages", (req, res) -> {
            res.type("text/plain");
            messages.forEach(m -> {
                try {
                    PrintStream outputStream = new PrintStream(res.raw().getOutputStream());
                    App.debugMessage(m, outputStream);
                } catch (MessagingException | IOException e) {
                    e.printStackTrace();
                }
            });
            return "";
        });
        get("/messages_json", (req, res) -> messages, gson::toJson);
    }
}
