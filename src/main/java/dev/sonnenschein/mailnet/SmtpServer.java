package dev.sonnenschein.mailnet;

import org.subethamail.smtp.server.SMTPServer;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class SmtpServer implements Runnable {
    private final List<MimeMessage> messages;
    private final IncomingMailsObservable observable;
    private final int port;

    public SmtpServer(List<MimeMessage> messages, IncomingMailsObservable observable, int port) {
        this.messages = messages;
        this.observable = observable;
        this.port = port;
    }

    @Override
    public void run() {
        final Session session = Session.getInstance(new Properties());

        SMTPServer server = SMTPServer
                .port(port)
                .messageHandler(
                        (context, from, to, data) -> {
                            try {
                                InputStream is = new ByteArrayInputStream(data);
                                MimeMessage message = new MimeMessage(session, is);
                                messages.add(message);
                                observable.notify(message);
                                DebugUtil.debugMessage(message, System.out);
                            } catch (MessagingException | IOException e) {
                                e.printStackTrace();
                            }
                        }
                )
                .build();

        server.start();
    }
}
