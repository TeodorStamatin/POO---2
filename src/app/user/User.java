package app.user;

import app.audio.Collections.AudioCollection;
import app.audio.Collections.Playlist;
import app.audio.Collections.PlaylistOutput;
import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.player.Player;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.searchBar.SearchBar;
import app.searchBar.SearchBarPage;
import app.utils.Enums;
import lombok.Getter;
import java.util.stream.Collectors;
import app.audio.Collections.Album;
import app.Admin;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * The type User.
 */
public class User {
    @Getter
    private String username;
    @Getter
    private int age;
    @Getter
    private String city;
    @Getter
    private ArrayList<Playlist> playlists;
    @Getter
    private ArrayList<Song> likedSongs;
    @Getter
    private ArrayList<Playlist> followedPlaylists;
    @Getter
    private final Player player;
    @Getter
    private final SearchBar searchBar;
    private final SearchBarPage searchBarPage;
    private boolean lastSearched;
    @Getter
    public boolean connectionStatus = true;
    @Getter
    @Setter
    public String page = "HomePage";
    public String lastSearchType = "";
    @Getter
    public String lastLoaded = "";

    /**
     * Instantiates a new User.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     */
    public User(final String username, final int age, final String city) {
        this.username = username;
        this.age = age;
        this.city = city;
        playlists = new ArrayList<>();
        likedSongs = new ArrayList<>();
        followedPlaylists = new ArrayList<>();
        player = new Player();
        searchBar = new SearchBar(username);
        searchBarPage = new SearchBarPage(username);
        lastSearched = false;
    }

    /**
     * Search array list.
     *
     * @param filters the filters
     * @param type    the type
     * @return the array list
     */
    public ArrayList<String> search(final Filters filters, final String type) {
        searchBar.clearSelection();
        player.stop();
        lastSearchType = type;
        lastLoaded = "";

        lastSearched = true;
        ArrayList<String> results = new ArrayList<>();
        if(type.equals("host") || type.equals("artist")) {
            List<String> tmp = searchBarPage.search(filters, type);

            for (String string : tmp) {
                results.add(string);
            }
        } else {
            List<LibraryEntry> libraryEntries = searchBar.search(filters, type);

            for (LibraryEntry libraryEntry : libraryEntries) {
                results.add(libraryEntry.getName());
            }
        }
        return results;
    }

    /**
     * Select string.
     *
     * @param itemNumber the item number
     * @return the string
     */
    public String select(final int itemNumber) {
        if (!lastSearched) {
            return "Please conduct a search before making a selection.";
        }

        lastSearched = false;

        if(lastSearchType.equals("host") || lastSearchType.equals("artist")) {
            String selected = searchBarPage.select(itemNumber);

            if (selected == null) {
                return "The selected ID is too high.";
            }

            page = selected;

            return "Successfully selected %s's page.".formatted(selected);
        } else {
            LibraryEntry selected = searchBar.select(itemNumber);

            if (selected == null) {
                return "The selected ID is too high.";
            }

            return "Successfully selected %s.".formatted(selected.getName());
        }
    }

    /**
     * Load string.
     *
     * @return the string
     */
    public String load() {
        if (searchBar.getLastSelected() == null) {
            return "Please select a source before attempting to load.";
        }

        if (!searchBar.getLastSearchType().equals("song")
            && ((AudioCollection) searchBar.getLastSelected()).getNumberOfTracks() == 0) {
            return "You can't load an empty audio collection!";
        }

        player.setSource(searchBar.getLastSelected(), searchBar.getLastSearchType());
        lastLoaded = searchBar.getLastSelected().getName();
        searchBar.clearSelection();

        player.pause();

        return "Playback loaded successfully.";
    }

    /**
     * Play pause string.
     *
     * @return the string
     */
    public String playPause() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to pause or resume playback.";
        }

        player.pause();

        if (player.getPaused()) {
            return "Playback paused successfully.";
        } else {
            return "Playback resumed successfully.";
        }
    }

    /**
     * Repeat string.
     *
     * @return the string
     */
    public String repeat() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before setting the repeat status.";
        }

        Enums.RepeatMode repeatMode = player.repeat();
        String repeatStatus = "";

        switch (repeatMode) {
            case NO_REPEAT -> {
                repeatStatus = "no repeat";
            }
            case REPEAT_ONCE -> {
                repeatStatus = "repeat once";
            }
            case REPEAT_ALL -> {
                repeatStatus = "repeat all";
            }
            case REPEAT_INFINITE -> {
                repeatStatus = "repeat infinite";
            }
            case REPEAT_CURRENT_SONG -> {
                repeatStatus = "repeat current song";
            }
            default -> {
                repeatStatus = "";
            }
        }

        return "Repeat mode changed to %s.".formatted(repeatStatus);
    }

    /**
     * Shuffle string.
     *
     * @param seed the seed
     * @return the string
     */
    public String shuffle(final Integer seed) {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before using the shuffle function.";
        }

        if (!player.getType().equals("playlist")) {
            return "The loaded source is not a playlist.";
        }

        player.shuffle(seed);

        if (player.getShuffle()) {
            return "Shuffle function activated successfully.";
        }
        return "Shuffle function deactivated successfully.";
    }

    /**
     * Forward string.
     *
     * @return the string
     */
    public String forward() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to forward.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        player.skipNext();

        return "Skipped forward successfully.";
    }

    /**
     * Backward string.
     *
     * @return the string
     */
    public String backward() {
        if (player.getCurrentAudioFile() == null) {
            return "Please select a source before rewinding.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        player.skipPrev();

        return "Rewound successfully.";
    }

    /**
     * Like string.
     *
     * @return the string
     */
    public String like() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before liking or unliking.";
        }

        if (!player.getType().equals("song") && !player.getType().equals("playlist")) {
            return "Loaded source is not a song.";
        }

        Song song = (Song) player.getCurrentAudioFile();

        if (likedSongs.contains(song)) {
            likedSongs.remove(song);
            song.dislike();

            return "Unlike registered successfully.";
        }

        likedSongs.add(song);
        song.like();
        return "Like registered successfully.";
    }

    /**
     * Next string.
     *
     * @return the string
     */
    public String next() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        player.next();

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        return "Skipped to next track successfully. The current track is %s."
                .formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * Prev string.
     *
     * @return the string
     */
    public String prev() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before returning to the previous track.";
        }

        player.prev();

        return "Returned to previous track successfully. The current track is %s."
                .formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * Create playlist string.
     *
     * @param name      the name
     * @param timestamp the timestamp
     * @return the string
     */
    public String createPlaylist(final String name, final int timestamp) {
        if (playlists.stream().anyMatch(playlist -> playlist.getName().equals(name))) {
            return "A playlist with the same name already exists.";
        }

        playlists.add(new Playlist(name, username, timestamp));

        return "Playlist created successfully.";
    }

    /**
     * Add remove in playlist string.
     *
     * @param id the id
     * @return the string
     */
    public String addRemoveInPlaylist(final int id) {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before adding to or removing from the playlist.";
        }

        if (player.getType().equals("podcast")) {
            return "The loaded source is not a song.";
        }

        if (id > playlists.size()) {
            return "The specified playlist does not exist.";
        }

        Playlist playlist = playlists.get(id - 1);

        if (playlist.containsSong((Song) player.getCurrentAudioFile())) {
            playlist.removeSong((Song) player.getCurrentAudioFile());
            return "Successfully removed from playlist.";
        }

        playlist.addSong((Song) player.getCurrentAudioFile());
        return "Successfully added to playlist.";
    }

    /**
     * Switch playlist visibility string.
     *
     * @param playlistId the playlist id
     * @return the string
     */
    public String switchPlaylistVisibility(final Integer playlistId) {
        if (playlistId > playlists.size()) {
            return "The specified playlist ID is too high.";
        }

        Playlist playlist = playlists.get(playlistId - 1);
        playlist.switchVisibility();

        if (playlist.getVisibility() == Enums.Visibility.PUBLIC) {
            return "Visibility status updated successfully to public.";
        }

        return "Visibility status updated successfully to private.";
    }

    /**
     * Show playlists array list.
     *
     * @return the array list
     */
    public ArrayList<PlaylistOutput> showPlaylists() {
        ArrayList<PlaylistOutput> playlistOutputs = new ArrayList<>();
        for (Playlist playlist : playlists) {
            playlistOutputs.add(new PlaylistOutput(playlist));
        }

        return playlistOutputs;
    }

    /**
     * Follow string.
     *
     * @return the string
     */
    public String follow() {
        LibraryEntry selection = searchBar.getLastSelected();
        String type = searchBar.getLastSearchType();

        if (selection == null) {
            return "Please select a source before following or unfollowing.";
        }

        if (!type.equals("playlist")) {
            return "The selected source is not a playlist.";
        }

        Playlist playlist = (Playlist) selection;

        if (playlist.getOwner().equals(username)) {
            return "You cannot follow or unfollow your own playlist.";
        }

        if (followedPlaylists.contains(playlist)) {
            followedPlaylists.remove(playlist);
            playlist.decreaseFollowers();

            return "Playlist unfollowed successfully.";
        }

        followedPlaylists.add(playlist);
        playlist.increaseFollowers();


        return "Playlist followed successfully.";
    }

    /**
     * Gets player stats.
     *
     * @return the player stats
     */
    public PlayerStats getPlayerStats() {
        return player.getStats();
    }

    /**
     * Show preferred songs array list.
     *
     * @return the array list
     */
    public ArrayList<String> showPreferredSongs() {
        ArrayList<String> results = new ArrayList<>();
        for (AudioFile audioFile : likedSongs) {
            results.add(audioFile.getName());
        }

        return results;
    }

    /**
     * Gets preferred genre.
     *
     * @return the preferred genre
     */
    public String getPreferredGenre() {
        String[] genres = {"pop", "rock", "rap"};
        int[] counts = new int[genres.length];
        int mostLikedIndex = -1;
        int mostLikedCount = 0;

        for (Song song : likedSongs) {
            for (int i = 0; i < genres.length; i++) {
                if (song.getGenre().equals(genres[i])) {
                    counts[i]++;
                    if (counts[i] > mostLikedCount) {
                        mostLikedCount = counts[i];
                        mostLikedIndex = i;
                    }
                    break;
                }
            }
        }

        String preferredGenre = mostLikedIndex != -1 ? genres[mostLikedIndex] : "unknown";
        return "This user's preferred genre is %s.".formatted(preferredGenre);
    }

    /**
     * Simulate time.
     *
     * @param time the time
     */
    public void simulateTime(final int time) {
        player.simulatePlayer(time);
    }

    public String switchConnectionStatus() {
        connectionStatus = !connectionStatus;
        return "%s has changed status successfully." .formatted(username);
    }

    public String printCurrentPage() {
        StringBuilder output = new StringBuilder();

        switch (page) {
            case "HomePage" -> {
                output.append("Liked songs:\n\t[").append(likedSongsToString()).append("]\n\n");
                output.append("Followed playlists:\n\t[").append(followedPlaylistsToString()).append("]");
            }
            case "LikedContentPage" -> {
                output.append("Liked Songs:\n\t").append(likedSongsDetailsToString()).append("\n\n");
                output.append("Followed Playlists:\n\t").append(followedPlaylistsDetailsToString()).append("\n");
            }
            default -> {
                switch (lastSearchType) {
                    case "artist":
                        Artist artist = Admin.getArtist(page);

                        String albumsString = albumsToString(artist);
                        String merchDetailsString = merchDetailsToString(artist);
                        String eventDetailsString = eventDetailsToString(artist);

                        output.append("Albums:\n\t[").append(albumsString).append("]\n\n");
                        output.append("Merch:\n\t[").append(merchDetailsString).append("]\n\n");
                        output.append("Events:\n\t[").append(eventDetailsString).append("]");

                        break;
                    case "host":
                        Host host = Admin.getHost(page);

//                        String podcastsString = podcastsToString(host);
//                        String announcementsDetailsString = announcementsDetailsToString(host);

                        break;
                    default:
                        break;
                }
            }
        }

        return output.toString();
    }

    private String likedSongsToString() {
        return likedSongs.stream()
                .map(AudioFile::getName)
                .collect(Collectors.joining(", "));
    }

    private String likedSongsDetailsToString() {
        return likedSongs.stream()
                .map(song -> song.getName() + " - " + song.getArtist())
                .collect(Collectors.joining(", "));
    }

    private String followedPlaylistsToString() {
        return followedPlaylists.stream()
                .map(Playlist::getName)
                .collect(Collectors.joining(", "));
    }

    private String followedPlaylistsDetailsToString() {
        return followedPlaylists.stream()
                .map(playlist -> playlist.getName() + " - " + playlist.getOwner())
                .collect(Collectors.joining(", "));
    }

    private String albumsToString(Artist artist) {
        return artist.getAlbums().stream()
                .map(Album::getName)
                .collect(Collectors.joining(", "));
    }

    private String merchDetailsToString(Artist artist) {
        return artist.getMerch().stream()
                .map(merchandise -> merchandise.getName() + " - " + merchandise.getPrice() + ":\n\t" + merchandise.getDescription())
                .collect(Collectors.joining(", "));
    }

    private String eventDetailsToString(Artist artist) {
        return artist.getEvents().stream()
                .map(event -> event.getName() + " - " + event.getDate() + ":\n\t" + event.getDescription())
                .collect(Collectors.joining(", "));
    }

//    private String podcastsToString(Host host) {
//        return host.getPodcasts().stream()
//                .map(Podcast::getName)
//                .collect(Collectors.joining(", "));
//    }
//
//    private String announcementsDetailsToString(Host host) {
//        return host.getAnnouncements().stream()
//                .map(announcement -> announcement.getName() + "\n\t" + announcement.getDescription())
//                .collect(Collectors.joining(", "));
//    }
}
