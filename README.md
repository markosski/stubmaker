# stubmaker

FakeGen is a small utility that generates a stub implementations of annotated interfaces. 
FakeGen can be useful in following scenarios:
* there is a need to mock beans in tests
* there is a need to fake beans in component tests to prevent executing code that interacts with outside world

## Usage

```java
import stubmaker.annotation.ImplementStub;
import java.util.Optional;

@ImplementStub
public interface UserRepo {
    record User(String id, String fullName) {}
    record NewUser(String fullName) {}
    
    Optional<User> get(String id);
    void create(NewUser newUser);
    void delete(String id);
}
```

Class of name `UserRepoStub` will be generated that implements `UserRepo` interface.

Following methods will be available on `UserRepoStub`:

`public Optional<UserRepo.User> get(String id)`

`public void when_get(String id, Optional<UserRepo.User> thenReturn)`

`public void when_get(Function<getParams, Optional<UserRepo.User>> thenApply)`

`public void create(UserRepo.NewUser newUser)`

`public void delete(String id)`
