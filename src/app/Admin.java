package app;

import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Collections.Album;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.player.Player;
import app.user.Artist;
import app.user.Host;
import app.user.User;
import fileio.input.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * The type Admin.
 */
public final class Admin {
    @Getter
    private static List<User> users = new ArrayList<>();
    private static List<Song> songs = new ArrayList<>();
    private static List<Podcast> podcasts = new ArrayList<>();
    private static List<Artist> artists = new ArrayList<>();
    private static List<Host> hosts = new ArrayList<>();
    private static int timestamp = 0;
    private static final int LIMIT = 5;

    private Admin() {
    }

    /**
     * Sets users.
     *
     * @param userInputList the user input list
     */
    public static void setUsers(final List<UserInput> userInputList) {
        users = new ArrayList<>();
        for (UserInput userInput : userInputList) {
            users.add(new User(userInput.getUsername(), userInput.getAge(), userInput.getCity()));
        }
    }

    /**
     * Sets songs.
     *
     * @param songInputList the song input list
     */
    public static void setSongs(final List<SongInput> songInputList) {
        songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }


    /**
     * Sets podcasts.
     *
     * @param podcastInputList the podcast input list
     */
    public static void setPodcasts(final List<PodcastInput> podcastInputList) {
        podcasts = new ArrayList<>();
        for (PodcastInput podcastInput : podcastInputList) {
            List<Episode> episodes = new ArrayList<>();
            for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
                episodes.add(new Episode(episodeInput.getName(),
                                         episodeInput.getDuration(),
                                         episodeInput.getDescription()));
            }
            podcasts.add(new Podcast(podcastInput.getName(), podcastInput.getOwner(), episodes));
        }
    }

    /**
     * Gets songs.
     *
     * @return the songs
     */
    public static List<Song> getSongs() {
        return new ArrayList<>(songs);
    }

    /**
     * Gets podcasts.
     *
     * @return the podcasts
     */
    public static List<Podcast> getPodcasts() {
        return new ArrayList<>(podcasts);
    }

    /**
     * Gets playlists.
     *
     * @return the playlists
     */
    public static List<Playlist> getPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        for (User user : users) {
            playlists.addAll(user.getPlaylists());
        }
        return playlists;
    }

    /**
     * Gets user.
     *
     * @param username the username
     * @return the user
     */
    public static User getUser(final String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Update timestamp.
     *
     * @param newTimestamp the new timestamp
     */
    public static void updateTimestamp(final int newTimestamp) {
        int elapsed = newTimestamp - timestamp;
        timestamp = newTimestamp;
        if (elapsed == 0) {
            return;
        }

        for (User user : users) {
            if(user.connectionStatus)
                user.simulateTime(elapsed);
        }
    }

    /**
     * Gets top 5 songs.
     *
     * @return the top 5 songs
     */
    public static List<String> getTop5Songs() {
        List<Song> sortedSongs = new ArrayList<>(songs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= LIMIT) {
                break;
            }
            topSongs.add(song.getName());
            count++;
        }
        return topSongs;
    }

    /**
     * Gets top 5 playlists.
     *
     * @return the top 5 playlists
     */
    public static List<String> getTop5Playlists() {
        List<Playlist> sortedPlaylists = new ArrayList<>(getPlaylists());
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers)
                .reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        List<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= LIMIT) {
                break;
            }
            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }

    public static List<String> getOnlineUsers() {
        List<String> onlineUsers = new ArrayList<>();
        for (User user : users) {
            if (user.connectionStatus) {
                onlineUsers.add(user.getUsername());
            }
        }
        return onlineUsers;
    }

    /**
     * Reset.
     */
    public static void reset() {
        users = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        artists = new ArrayList<>();
        hosts = new ArrayList<>();
        timestamp = 0;
    }

    public static String addArtist(final Artist artist) {

        for (Artist artist1 : artists) {
            if (artist1.getUsername().equals(artist.getUsername())) {
                return "The username %s is already taken.".formatted(artist.getUsername());
            }
        }
        for (Host host : hosts) {
            if (host.getUsername().equals(artist.getUsername())) {
                return "The username %s is already taken.".formatted(artist.getUsername());
            }
        }
        for (User user : users) {
            if (user.getUsername().equals(artist.getUsername())) {
                return "The username %s is already taken.".formatted(artist.getUsername());
            }
        }

        artists.add(artist);
        return "The username %s has been added successfully.".formatted(artist.getUsername());

    }

    public static void addHost(final Host host) {
        hosts.add(host);
    }

    public static List<Artist> getArtists() {
        return new ArrayList<>(artists);
    }

    public static List<Host> getHosts() {
        return new ArrayList<>(hosts);
    }

    public static String addUser(final User user) {

        for (User user1 : users) {
            if (user1.getUsername().equals(user.getUsername())) {
                return "The username %s is already taken.".formatted(user.getUsername());
            }
        }

        users.add(user);
        return "The username %s has been added successfully.".formatted(user.getUsername());

    }

    public static Artist getArtist(final String username) {
        for (Artist artist : artists) {
            if (artist.getUsername().equals(username)) {
                return artist;
            }
        }
        return null;
    }

    public static Host getHost(final String username) {
        for (Host host : hosts) {
            if (host.getUsername().equals(username)) {
                return host;
            }
        }
        return null;
    }

    public static String addAlbum(final CommandInput commandInput) {
        String username = commandInput.getUsername();
        String albumName = commandInput.getName();
        int releaseYear = commandInput.getReleaseYear();
        String description = commandInput.getDescription();

        Album album = new Album(albumName, releaseYear, description, username);

        for(Artist artist : artists) {
            if (artist.getUsername().equals(username)) {
                if(artist.albumExists(albumName)) {
                    return "%s has another album with the same name.".formatted(username);
                }
                artist.addAlbum(album);
                List<SongInput> songInputList = commandInput.getSongs();
                for (SongInput songInput : songInputList) {
                    Song new_song = new Song(songInput.getName(), songInput.getDuration(),
                            albumName, songInput.getTags(), songInput.getLyrics(),
                            songInput.getGenre(), releaseYear, username);
                    if(album.songExists(new_song.getName())) {
                        return "%s has the same song at least twice in this album.".formatted(username);
                    }
                    album.addSong(new_song);
                    songs.add(new_song);
                }
                return "%s has added new album successfully.".formatted(username);
            }
        }

        for (Host host : hosts) {
            if (host.getUsername().equals(username)) {
                return "The username %s is not an artist".formatted(username);
            }
        }
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return "The username %s is not an artist".formatted(username);
            }
        }
        return "The username %s is not an artist".formatted(username);
    }

    public static String addEvent(final String name, final String date, final String description, final String username) {

        for (Host host : Admin.getHosts()) {
            if (host.getUsername().equals(username)) {
                return "%s is not an artist.".formatted(username);
            }
        }
        for (User user : Admin.getUsers()) {
            if (user.getUsername().equals(username)) {
                return "%s is not an artist.".formatted(username);
            }
        }
        for (Artist artist : Admin.getArtists()) {
            if (artist.getUsername().equals(username)) {
                String message = artist.addEvent(name, date, description);
                return message;
            }
        }
        return "The username %s doesn't exist.".formatted(username);
    }

    public static String addMerch(final String name, final Integer price, final String description, final String username) {

        for (Host host : Admin.getHosts()) {
            if (host.getUsername().equals(username)) {
                return "%s is not an artist.".formatted(username);
            }
        }
        for (User user : Admin.getUsers()) {
            if (user.getUsername().equals(username)) {
                return "%s is not an artist.".formatted(username);
            }
        }
        for (Artist artist : Admin.getArtists()) {
            if (artist.getUsername().equals(username)) {
                String message = artist.addMerch(name, price, description);
                return message;
            }
        }
        return "The username %s doesn't exist.".formatted(username);
    }

    public static List<Album> getAlbums() {
        List<Album> albums = new ArrayList<>();
        for (Artist artist : artists) {
            albums.addAll(artist.getAlbums());
        }
        return albums;
    }

    public static List<String> getAllUsers() {
        List<String> allUsers = new ArrayList<>();
        for (User user : users) {
            allUsers.add(user.getUsername());
        }
        for(Artist artist : artists) {
            allUsers.add(artist.getUsername());
        }
        for(Host host : hosts) {
            allUsers.add(host.getUsername());
        }
        return allUsers;
    }

    public static String deleteUser(String username) {

        for (User user : users) {
            if (user.getUsername().equals(username)) {
                users.remove(user);
                return "%s was successfully deleted.".formatted(username);
            }
        }

        for (Artist artist : artists) {
            if (artist.getUsername().equals(username)) {
                for(User user : users) {
                    String curr = user.lastLoaded;
                    if(user.getPlayer().getCurrentAudioFile() != null) {
                        for(Album album : artist.getAlbums()) {
                            if(album.getName().equals(curr)) {
                                return "%s can't be deleted.".formatted(username);
                            }
                            for(Song song : album.getSongs()) {
                                if(song.getName().equals(curr)) {
                                    return "%s can't be deleted.".formatted(username);
                                }
                            }
                        }
                    }
                    if(user.getPage().equals(username)) {
                        user.setPage("Home Page");
                    }
                    Iterator<Song> iterator = user.getLikedSongs().iterator();
                    while (iterator.hasNext()) {
                        Song song = iterator.next();
                        if (song.getArtist().equals(username)) {
                            iterator.remove();
                        }
                    }
                    List<Song> list = Admin.getSongs();
                    for(Song song : list) {
                        if(song.getArtist().equals(username)) {
                            Admin.removeSong(song.getName());
                        }
                    }
                }
                artists.remove(artist);
                return "%s was successfully deleted.".formatted(username);
            }
        }
        for (Host host : hosts) {
            if (host.getUsername().equals(username)) {
                hosts.remove(host);
                return "The username %s has been removed successfully.".formatted(username);
            }
        }
        return "The username %s doesn't exist.".formatted(username);
    }

    public static void removeSong(String songName) {
        for (Song song : songs) {
            if (song.getName().equals(songName)) {
                songs.remove(song);
                return;
            }
        }
    }
}
