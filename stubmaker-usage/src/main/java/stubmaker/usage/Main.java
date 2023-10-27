package stubmaker.usage;

import java.util.Optional;
import java.util.UUID;
import stubmaker.usage.EmailServiceStub;
import stubmaker.usage.UserRepoStub;

public class Main {
    public static void main(String[] args) {
        var emailService = new EmailServiceStub();
        var userRepo = new UserRepoStub();
        userRepo.when_get("100", Optional.of(new UserRepo.User("100", "1", "Marcin K")));
        userRepo.when_get("101", Optional.of(new UserRepo.User("101", "1", "John Wick")));
        userRepo.when_get((params) -> Optional.empty());

        userRepo.when_create((params, allData) -> {
            var id = UUID.randomUUID().toString();
            var param = new UserRepoStub.GetParams(id);
            var user = new UserRepo.User(id, params.newUser().accountId(), params.newUser().fullName());
            allData.data_get().put(param, Optional.of(user));
            return id;
        });

        userRepo.when_delete((params, allData) -> {
            var getParams = new UserRepoStub.GetParams(params.id());
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

        var component = new SendEmailComponent(userRepo, emailService);

        component.execute("100");
        component.execute("101");
        component.execute("102");
        userRepo.create(new UserRepo.NewUser("1", "Melissa Maida"));
        userRepo.delete("100");
        component.execute("1");
    }

    public static class SendEmailComponent {
        private final UserRepo userRepo;
        private final EmailService emailService;
        public SendEmailComponent(UserRepo userRepo, EmailService emailService) {
            this.userRepo = userRepo;
            this.emailService = emailService;
        }

        public void execute(String userId) {
            var allUsers = userRepo.getAll("1");
            System.out.println(allUsers);
            var user = userRepo.get(userId);

            user.ifPresent((u) -> {
                var subject = "Thank you!";
                var message = "Hello %s, thank you for visit in our store.".formatted(u.fullName());
                emailService.send("foo@gmail.com", "customer@gmail.com", subject, message);
            });
        }
    }
}