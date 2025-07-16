package rs.ac.uns.ftn.informatika.jpa.mq.custom;

import java.util.concurrent.BlockingQueue;

public interface MessageQueue {
    void send(Message message) throws InterruptedException;
    Message receive() throws InterruptedException;
    void subscribe(String consumerId, BlockingQueue<Message> queue);
    void unsubscribe(String consumerId);
}