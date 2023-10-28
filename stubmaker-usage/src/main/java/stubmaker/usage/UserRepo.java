package stubmaker.usage;

import stubmaker.annotation.MakeStub;

import java.util.List;
import java.util.Optional;

@MakeStub
public interface UserRepo {
    record User(String id, String accountId, String fullName) {}
    record NewUser(String accountId, String fullName) {}

    Optional<User> get(String id);
    Optional<User> get(String id, String accountId);
    List<User> getAll(String accountId);
    String create(NewUser newUser);
    void delete(String id);
}
