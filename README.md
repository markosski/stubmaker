# stubmaker

Stubmaker is a simple library that generates (no runtime reflection) a stub implementations for annotated interfaces. 
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

Stub will be generated under the same package as your interface. 
Depending on whether interface methods have parameters or not, `when_` methods will be generated on the stub to allow defining responses for given arguments.
In above example `when_` methods are only generated for `get(...)` because this is the only method that returns a value. Generated stub will also log with slf4j any method calls including arguments.

Set stub interactions

```java
var userRepo = new UserRepoStub();
userRepo.when_get("100", Optional.of(new UserRepo.User("100", "Marcin K")));
userRepo.when_get("101", Optional.of(new UserRepo.User("101", "John Wick")));
userRepo.when_get((params) -> Optional.empty()); // for any other input

// somewhere else

doSomethingWithUser(String userId, UserRepo userRepo) {
  //...
  var user = userRepo.get(userId);
  //...
}
```

See more examples in `annotation-usage` module.



