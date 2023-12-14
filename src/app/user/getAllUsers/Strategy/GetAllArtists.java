// GetAllArtists.java
package app.user.getAllUsers.Strategy;

import app.Admin;
import app.user.Artist;

import java.util.List;
import java.util.stream.Collectors;

public class GetAllArtists implements GetAllUsersStrategy {

    /**
     *
     * @return
     */
    @Override
    public List<String> getAllUsers() {
        return Admin.getArtists().stream()
                .map(Artist::getUsername)
                .collect(Collectors.toList());
    }
}
