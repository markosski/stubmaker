package fakegen.usage;

import fakegen.annotation.ImplementStub;
import java.util.Optional;

@ImplementStub
public interface UserRepo {
    record User(String id, String fullName) {}
    record NewUser(String fullName) {}
    Optional<User> get(String id);
    void create(NewUser newUser);
    void delete(String id);
}
