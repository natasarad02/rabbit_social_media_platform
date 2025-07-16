package rs.ac.uns.ftn.informatika.jpa.mq.custom;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DirectQueue implements MessageQueue {
    private final BlockingQueue<Message> queue = new LinkedBlockingQueue<>();

    @Override
    public void send(Message message) throws InterruptedException {
        queue.put(message);
    }

    @Override
    public Message receive() throws InterruptedException {
        return queue.take();
    }

    @Override public void subscribe(String consumerId, BlockingQueue<Message> queue) { /* Not used for Direct */ }
    @Override public void unsubscribe(String consumerId) { /* Not used for Direct */ }
}