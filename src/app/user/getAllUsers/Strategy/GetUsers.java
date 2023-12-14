package app.user.getAllUsers.Strategy;

import app.Admin;
import app.user.User;
import app.user.Artist;
import app.user.Host;

import java.util.List;
import java.util.stream.Collectors;

public class GetUsers {
    private List<GetAllUsersStrategy> strategies;

    public GetUsers(List<GetAllUsersStrategy> strategies) {
        this.strategies = strategies;
    }

    public List<String> getAllUsers() {
        return strategies.stream()
                .flatMap(strategy -> strategy.getAllUsers().stream())
                .collect(Collectors.toList());
    }
}
