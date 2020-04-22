package dev.sonnenschein.mailnet;

import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

public class IncomingMailsObservable implements Runnable {

    private final List<Consumer<MimeMessage>> observers = Collections.synchronizedList(new ArrayList<>());
    private final BlockingQueue<MimeMessage> notificationQueue = new ArrayBlockingQueue<>(10);

    public void observe(Consumer<MimeMessage> observer) {
        observers.add(observer);
    }

    // best names ever
    public void unObserve(Consumer<MimeMessage> observer) {
        observers.remove(observer);
    }

    public void notify(MimeMessage mail) {
        notificationQueue.add(mail);
    }

    @Override
    public void run() {
        while (true) {
            try {
                MimeMessage message = notificationQueue.take();
                observers.forEach(o -> o.accept(message));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
