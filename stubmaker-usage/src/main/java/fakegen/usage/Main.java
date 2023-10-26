package fakegen.usage;

import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        var emailService = new fakegen.usage.EmailServiceStub();
        var userRepo = new fakegen.usage.UserRepoStub();
        userRepo.when_get("100", Optional.of(new UserRepo.User("100", "Marcin K")));
        userRepo.when_get("101", Optional.of(new UserRepo.User("101", "John Wick")));
        userRepo.when_get((params) -> Optional.empty());

        userRepo.create(new UserRepo.NewUser(""));

        var component = new SendEmailComponent(userRepo, emailService);

        component.execute("100");
        component.execute("101");
        component.execute("102");
    }

    public static class SendEmailComponent {
        private final UserRepo userRepo;
        private final EmailService emailService;
        public SendEmailComponent(UserRepo userRepo, EmailService emailService) {
            this.userRepo = userRepo;
            this.emailService = emailService;
        }

        public void execute(String userId) {
            var user = userRepo.get(userId);
            user.ifPresent((u) -> {
                var subject = "Thank you!";
                var message = "Hello %s, thank you for visit in our store.".formatted(u.fullName());
                emailService.send("foo@gmail.com", "customer@gmail.com", subject, message);
            });
        }
    }
}