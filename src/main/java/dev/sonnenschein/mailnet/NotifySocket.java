package dev.sonnenschein.mailnet;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.function.Consumer;

@WebSocket
public class NotifySocket {

    private final IncomingMailsObservable observable;
    private Consumer<MimeMessage> handler;

    public NotifySocket(IncomingMailsObservable observable) {
        this.observable = observable;
    }

    @OnWebSocketConnect
    public void connected(Session session) {
        handler = m -> {
            try {
                // todo, maybe just send the entire mail here....
                session.getRemote().sendString("got one, please refresh");
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        observable.observe(handler);
    }

    @OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
        observable.unObserve(handler);
    }
}
