package fakegen.annotation;

@ImplementStub
public interface UserRepo {
    record User(String id, String fullName) {}
    record NewUser(String fullName) {}

    User get(String id);
    void create(NewUser newUser);
    void delete(String id);
}
