// GetAllHosts.java
package app.user.getAllUsers.Strategy;

import app.Admin;
import app.user.Host;

import java.util.List;
import java.util.stream.Collectors;

public class GetAllHosts implements GetAllUsersStrategy {
    @Override
    public List<String> getAllUsers() {
        return Admin.getHosts().stream()
                .map(Host::getUsername)
                .collect(Collectors.toList());
    }
}
