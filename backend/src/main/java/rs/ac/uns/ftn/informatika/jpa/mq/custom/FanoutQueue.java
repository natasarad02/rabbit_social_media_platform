package rs.ac.uns.ftn.informatika.jpa.mq.custom;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FanoutQueue implements MessageQueue {
    private final ConcurrentMap<String, BlockingQueue<Message>> subscribers = new ConcurrentHashMap<>();

    @Override
    public void send(Message message) throws InterruptedException {
        for (BlockingQueue<Message> queue : subscribers.values()) {
            queue.put(message);
        }
    }

    @Override
    public Message receive() {
        throw new UnsupportedOperationException("Fanout queues require subscription.");
    }

    @Override
    public void subscribe(String consumerId, BlockingQueue<Message> queue) {
        subscribers.put(consumerId, queue);
    }

    @Override
    public void unsubscribe(String consumerId) {
        subscribers.remove(consumerId);
    }
}