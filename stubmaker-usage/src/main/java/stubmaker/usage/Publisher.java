package stubmaker.usage;

import stubmaker.annotation.MakeStub;

@MakeStub
public interface Publisher {
    record Event<T>(String id, T payload) {}

    void publish(String topic, Event<?> event);
}
