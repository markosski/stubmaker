package stubmaker.usage;

import stubmaker.annotation.ImplementStub;

@ImplementStub
public interface Publisher {
    record Event<T>(String id, T payload) {}

    void publish(String topic, Event<?> event);
}
