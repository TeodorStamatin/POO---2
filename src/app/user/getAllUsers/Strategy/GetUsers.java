package app.user.getAllUsers.Strategy;

import java.util.List;
import java.util.stream.Collectors;

public class GetUsers {
    private List<GetAllUsersStrategy> strategies;

    /**
     *
     * @param strategies
     */
    public GetUsers(final List<GetAllUsersStrategy> strategies) {
        this.strategies = strategies;
    }

    /**
     *
     * @return
     */
    public List<String> getAllUsers() {
        return strategies.stream()
                .flatMap(strategy -> strategy.getAllUsers().stream())
                .collect(Collectors.toList());
    }
}
