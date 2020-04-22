package dev.sonnenschein.mailnet;

import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class App {
    private final IncomingMailsObservable observable = new IncomingMailsObservable();
    private final List<MimeMessage> messages = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        System.out.println("hello");

        App app = new App();
        app.startMailObservable();
        app.startSMTPServer();
        app.startHTTPServer();
    }

    private void startMailObservable() {
        new Thread(observable, "Observable-Thread").start();
    }

    private void startHTTPServer() {
        new Thread(new HttpServer(messages, observable, 8080), "HTTP-Thread").start();
    }

    private void startSMTPServer() {
        new Thread(new SmtpServer(messages, observable, 25000), "SMTP-Thread").start();
    }

}
