package dev.sonnenschein.mailnet;

import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class App {

    private final List<MimeMessage> messages = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        System.out.println("hello");

        App app = new App();
        app.startSMTPServer();
        app.startHTTPServer();
    }

    private void startHTTPServer() {
        new Thread(new HttpServer(messages, 8080), "HTTP-Thread").start();
    }

    private void startSMTPServer() {
        new Thread(new SmtpServer(messages, 25000), "SMTP-Thread").start();
    }

}
