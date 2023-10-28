package stubmaker.usage;

import java.util.Optional;
import java.util.UUID;
import stubmaker.usage.EmailServiceStub;
import stubmaker.usage.UserRepoStub;

public class Main {
    public static void main(String[] args) {
        var emailService = new EmailServiceStub();
        var userRepo = UserRepoFactory.create();

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