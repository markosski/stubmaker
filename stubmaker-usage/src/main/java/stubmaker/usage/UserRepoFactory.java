package stubmaker.usage;

import java.util.Optional;
import java.util.UUID;

public class UserRepoFactory {
    public static UserRepo create() {
        var userRepo = new stubmaker.usage.UserRepoStub();
        // Defining initial data
        userRepo.when_get("100", Optional.of(new UserRepo.User("100", "1", "Marcin K")));
        userRepo.when_get("101", Optional.of(new UserRepo.User("101", "1", "John Wick")));
        userRepo.when_get("102", Optional.of(new UserRepo.User("102", "2", "Melissa M")));

        // If there is no match
        userRepo.when_get((params) -> Optional.empty());

        // Define interaction for overloaded method
        userRepo.when_get2((params, allData) ->
                allData.data_get().values().stream()
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .filter(x -> x.accountId().equals(params.accountId()) && x.id().equals(params.id()))
                        .findFirst()
        );

        // Define interaction for create method
        userRepo.when_create((params, allData) -> {
            var id = UUID.randomUUID().toString();
            var param = new stubmaker.usage.UserRepoStub.GetParams(id);
            var user = new UserRepo.User(id, params.newUser().accountId(), params.newUser().fullName());
            allData.data_get().put(param, Optional.of(user));
            return id;
        });

        // Define interaction for delete method
        userRepo.when_delete((params, allData) -> {
            var getParams = new stubmaker.usage.UserRepoStub.GetParams(params.id());
            allData.data_get().remove(getParams);
            return null;
        });

        userRepo.when_getAll(((params, allData) ->
                allData.data_get().entrySet().stream()
                        .filter(entry -> entry.getValue().stream().allMatch(x -> x.accountId().equals(params.accountId())))
                        .filter(entry -> entry.getValue().isPresent())
                        .map(entry -> entry.getValue().get())
                        .toList()
        ));
        return userRepo;
    }
}
