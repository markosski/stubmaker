package stubmaker.domain;

import org.junit.jupiter.api.Test;
import stubmaker.UserRepoFactory;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserRepoTest {
    @Test
    public void getUserTest() {
        var userRepo = UserRepoFactory.create();
        var user = userRepo.get("100");

        assertEquals(
                Optional.of(new UserRepo.User("100", "1", "Marcin K")),
                user
                );
    }

    @Test
    public void getAllUsersTest() {
        var userRepo = UserRepoFactory.create();
        var users = userRepo.getAll("1");

        assertEquals(
                2,
                users.size()
        );
    }

    @Test
    public void createUserTest() {
        var userRepo = UserRepoFactory.create();
        var userId = userRepo.create(new UserRepo.NewUser("1", "John Doe"));
        var users = userRepo.getAll("1");

        assertTrue(userId.length() > 0);

        assertEquals(
                3,
                users.size()
        );
    }

    @Test
    public void deleteUserTest() {
        var userRepo = UserRepoFactory.create();
        userRepo.delete("100");
        var user = userRepo.get("100");

        assertEquals(
                Optional.empty(),
                user
        );
    }
}
