# stubmaker

Stubmaker is a small utility that generates a stub implementations of annotated interfaces. 
Stubmaker can be useful in scenarios where there is a need to execute application in lower environment or under test with some componnets stubbed to prevent from communicating with external services, e.g. component that sends confirmation email.

## Usage

```xml
<dependency>
  <groupId>io.github.markosski</groupId>
  <artifactId>stubmaker-annotation</artifactId>
  <version>...</version>
</dependency>
```

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
