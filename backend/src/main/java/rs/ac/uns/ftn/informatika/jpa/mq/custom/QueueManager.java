package rs.ac.uns.ftn.informatika.jpa.mq.custom;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class QueueManager {
    private static final QueueManager INSTANCE = new QueueManager();
    private final ConcurrentMap<String, MessageQueue> queues = new ConcurrentHashMap<>();

    private QueueManager() {}

    public static QueueManager getInstance() {
        return INSTANCE;
    }

    public void createQueue(String queueName, QueueType type) {
        queues.computeIfAbsent(queueName, name -> {
            if (type == QueueType.DIRECT) {
                return new DirectQueue();
            } else {
                return new FanoutQueue();
            }
        });
    }

    public MessageQueue getQueue(String queueName) {
        return queues.get(queueName);
    }

    public enum QueueType {
        DIRECT,
        FANOUT
    }
}