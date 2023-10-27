package stubmaker.usage;

import stubmaker.annotation.ImplementStub;

import java.util.List;
import java.util.Optional;

@ImplementStub
public interface UserRepo {
    record User(String id, String accountId, String fullName) {}
    record NewUser(String accountId, String fullName) {}

    Optional<User> get(String id);
    List<User> getAll(String accountId);
    String create(NewUser newUser);
    void delete(String id);
}
