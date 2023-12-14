// GetAllUsers.java
package app.user.getAllUsers.Strategy;

import app.Admin;
import app.user.User;

import java.util.List;
import java.util.stream.Collectors;

public class GetAllUsers implements GetAllUsersStrategy {

    /**
     *
     * @return
     */
    @Override
    public List<String> getAllUsers() {
        return Admin.getUsers().stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
    }
}
