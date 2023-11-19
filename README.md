# stubmaker

Stubmaker is a simple library that generates (no runtime reflection) a stub implementations for annotated interfaces. 
Stubmaker can be useful in scenarios where there is a need to execute application in lower environment or under test with some componnets stubbed to prevent from communicating with external services, e.g. component that sends confirmation email.

## Usage

Note: Stubmaker requires JDK 17.

```xml
<dependency>
  <groupId>io.github.markosski</groupId>
  <artifactId>stubmaker-annotation</artifactId>
  <version>...</version>
</dependency>
```

Add following to `maven-compiler-plugin` plugin configuration

```xml
<configuration>
  <annotationProcessorPaths>
    <path>
      <groupId>io.github.markosski</groupId>
      <artifactId>stubmaker-annotation</artifactId>
      <version>...</version>
    </path>
   </annotationProcessorPaths>
</configuration>

```

Annotate your interface with `@MakeStub`

```java
import stubmaker.annotation.MakeStub;
import java.util.Optional;

@MakeStub
public interface UserRepo {
    record User(String id, String fullName) {}
    record NewUser(String fullName) {}
    
    Optional<User> get(String id);
    void create(NewUser newUser);
    void delete(String id);
}
```

* Stub class will be generated under the same package as your interface. 
* `when_` methods will be generated on the stub to allow defining responses for given arguments
* Generated classes can also function as Fakes by providing functions to modify internal data

Defining stub interactions

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

```java
var userRepo = new UserRepoStub();
        
userRepo.when_create((params, allData) -> {
    var id = UUID.randomUUID().toString(); // generate unique ID for user
    var param = new UserRepoStub.GetParams(id); // create param object, a key in data_get map
    var user = new UserRepo.User(id, params.newUser().accountId(), params.newUser().fullName()); // create user record
    allData.data_get().put(param, Optional.of(user)); // add user record
    return id;
});
```

See more usage examples in `annotation-usage` module.



