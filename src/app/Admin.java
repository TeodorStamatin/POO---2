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
import lombok.experimental.NonFinal;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import app.audio.Collections.Album;

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

    public static List<Album> getAllAlbums() {
        List<Album> albums = new ArrayList<>();
        for (Artist artist : artists) {
            albums.addAll(artist.getAlbums());
        }
        return albums;
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

    public static String switchConnection(final String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user.switchConnectionStatus();
            }
        }
        for(Artist artist : artists) {
            if (artist.getUsername().equals(username)) {
                return "%s is not a normal user.".formatted(username);
            }
        }
        for(Host host : hosts) {
            if (host.getUsername().equals(username)) {
                return "%s is not a normal user.".formatted(username);
            }
        }
        return "The username %s doesn't exist.".formatted(username);
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

    public static List<String> getTop5Albums() {
        List<Album> allAlbums = getAllAlbums();

        Collections.sort(allAlbums, Comparator
                .<Album, Integer>comparing(album -> album.getSongs().stream().mapToInt(Song::getLikes).sum())
                .reversed()
                .thenComparing(Album::getName));

        List<String> topAlbums = allAlbums.stream()
                .limit(5)
                .map(Album::getName)
                .collect(Collectors.toList());

        return topAlbums;
    }

    public static List<String> getTop5Artists() {
        List<Artist> allArtists = new ArrayList<>(artists);

        Collections.sort(allArtists, Comparator
                .<Artist, Integer>comparing(artist -> artist.getAlbums().stream().flatMap(album -> album.getSongs().stream())
                        .mapToInt(Song::getLikes).sum())
                .reversed()
                .thenComparing(Artist::getName));

        List<String> topArtists = allArtists.stream()
                .limit(5)
                .map(Artist::getName)
                .collect(Collectors.toList());

        return topArtists;
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
                List<SongInput> songInputList = commandInput.getSongs();
                for (SongInput songInput : songInputList) {
                    Song new_song = new Song(songInput.getName(), songInput.getDuration(),
                            albumName, songInput.getTags(), songInput.getLyrics(),
                            songInput.getGenre(), releaseYear, username);
                    if(album.songExists(new_song.getName())) {
                        return "%s has the same song at least twice in this album.".formatted(username);
                    }
                    album.addSong(new_song);
                }
                artist.addAlbum(album);
                List<Song> tmp = album.getSongs();
                for (Song song : tmp) {
                    songs.add(song);
                }
                return "%s has added new album successfully.".formatted(username);
            }
        }

        for (Host host : hosts) {
            if (host.getUsername().equals(username)) {
                return "%s is not an artist.".formatted(username);
            }
        }
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return "%s is not an artist.".formatted(username);
            }
        }
        return "The username %s doesn't exist.".formatted(username);
    }

    public static String addPodcast(final CommandInput commandInput) {

        String username = commandInput.getUsername();
        String podcastName = commandInput.getName();


        for(Host host : hosts) {
            if (host.getUsername().equals(username)) {
                if(host.podcastExists(podcastName)) {
                    return "%s has another podcast with the same name.".formatted(username);
                }

                List<Episode> episodeList = new ArrayList<>();

                List<EpisodeInput> episodeInputList = commandInput.getEpisodes();
                for (EpisodeInput episodeInput : episodeInputList) {
                    Episode new_episode = new Episode(episodeInput.getName(), episodeInput.getDuration(),
                            episodeInput.getDescription());
                    for(Episode episode : episodeList) {
                        if(episode.getName().equals(new_episode.getName())) {
                            return "%s has the same episode in this podcast.".formatted(username);
                        }
                    }
                    episodeList.add(new_episode);
                }
                Podcast podcast = new Podcast(podcastName, username, episodeList);
                host.podcasts.add(podcast);
                podcasts.add(podcast);
                return "%s has added new podcast successfully.".formatted(username);
            }
        }

        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return "%s is not a host.".formatted(username);
            }
        }
        for (Artist artist : artists) {
            if (artist.getUsername().equals(username)) {
                return "%s is not a host.".formatted(username);
            }
        }
        return "The username %s doesn't exist.".formatted(username);
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

    public static String addAnnouncement(final String name, final String username, final String description) {

        for (User user : Admin.getUsers()) {
            if (user.getUsername().equals(username)) {
                return "%s is not a host.".formatted(username);
            }
        }
        for (Artist artist : Admin.getArtists()) {
            if (artist.getUsername().equals(username)) {
                return "%s is not a host.".formatted(username);
            }
        }
        for (Host host : Admin.getHosts()) {
            if (host.getUsername().equals(username)) {
                String message = host.addAnnouncement(name, description);
                return message;
            }
        }
        return "The username %s doesn't exist.".formatted(username);
    }

    public static String removeAnnouncement(final String name, final String username) {

        for (User user : Admin.getUsers()) {
            if (user.getUsername().equals(username)) {
                return "%s is not a host.".formatted(username);
            }
        }
        for (Artist artist : Admin.getArtists()) {
            if (artist.getUsername().equals(username)) {
                return "%s is not a host.".formatted(username);
            }
        }
        for (Host host : Admin.getHosts()) {
            if (host.getUsername().equals(username)) {
                String message = host.removeAnnouncement(name);
                return message;
            }
        }
        return "The username %s doesn't exist.".formatted(username);
    }

    public static String removeEvent(final String name, final String username) {

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
                String message = artist.removeEvent(name);
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
                if(user.getPlayer().getCurrentAudioFile() != null) {
                    return "%s can't be deleted.".formatted(username);
                }
                List<Playlist> playlists = user.getPlaylists();
                for(Playlist playlist : playlists) {
                    for(User user1 : users) {
                        if(user1.lastLoaded.equals(playlist.getName()) && user1.getPlayer().getCurrentAudioFile() != null) {
                            return "%s can't be deleted.".formatted(username);
                        }
                    }
                }
                for (User user1 : users) {
                    Iterator<Playlist> iterator = user1.getFollowedPlaylists().iterator();
                    while (iterator.hasNext()) {
                        Playlist playlist = iterator.next();
                        if (playlist.getOwner().equals(username)) {
                            iterator.remove();
                        }
                    }
                }
                playlists = user.getFollowedPlaylists();
                for (Playlist playlist : playlists) {
                    playlist.decreaseFollowers();
                }
                users.remove(user);
                return "%s was successfully deleted.".formatted(username);
            }
        }

        for (Artist artist : artists) {
            if (artist.getUsername().equals(username)) {
                for (User user : users) {
                    String curr = user.lastLoaded;
                    if (user.getPlayer().getCurrentAudioFile() != null) {
                        for (Album album : artist.getAlbums()) {
                            if (album.getName().equals(curr)) {
                                return "%s can't be deleted.".formatted(username);
                            }
                            for (Song song : album.getSongs()) {
                                if (song.getName().equals(curr)) {
                                    return "%s can't be deleted.".formatted(username);
                                }
                            }
                            List<Playlist> playlists = user.getFollowedPlaylists();
                            for(Playlist playlist : playlists) {
                                if(playlist.getName().equals(curr)) {
                                    for(Song song : playlist.getSongs()) {
                                        if(user.getPlayer().getCurrentAudioFile() != null) {
                                            if(song.getName().equals(user.getPlayer().getCurrentAudioFile().getName())) {
                                                return "%s can't be deleted.".formatted(username);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (user.getPage().equals(username)) {
                        return "%s can't be deleted.".formatted(username);
                    }
                }
                for(User user1 : users) {
                    Iterator<Song> iterator = user1.getLikedSongs().iterator();
                    while (iterator.hasNext()) {
                        Song song = iterator.next();
                        if (song.getArtist().equals(username)) {
                            iterator.remove();
                        }
                    }
                    List<Song> list = Admin.getSongs();
                    for (Song song : list) {
                        if (song.getArtist().equals(username)) {
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
                for (User user : users) {
                    String curr = user.lastLoaded;
                    if (user.getPlayer().getCurrentAudioFile() != null) {
                        for (Podcast podcast : host.getPodcasts()) {
                            if (podcast.getName().equals(curr)) {
                                return "%s can't be deleted.".formatted(username);
                            }
                            for (Episode episode : podcast.getEpisodes()) {
                                if (episode.getName().equals(curr)) {
                                    return "%s can't be deleted.".formatted(username);
                                }
                            }
                        }
                    }
                    if (user.getPage().equals(username)) {
                        return "%s can't be deleted.".formatted(username);
                    }
                }
                hosts.remove(host);
                return "%s was successfully deleted.".formatted(username);
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

    public static String addHost(final Host host) {

        for (Artist artist : artists) {
            if (artist.getUsername().equals(host.getUsername())) {
                return "The username %s is already taken.".formatted(host.getUsername());
            }
        }
        for (Host host1 : hosts) {
            if (host1.getUsername().equals(host.getUsername())) {
                return "The username %s is already taken.".formatted(host.getUsername());
            }
        }
        for (User user : users) {
            if (user.getUsername().equals(host.getUsername())) {
                return "The username %s is already taken.".formatted(host.getUsername());
            }
        }

        hosts.add(host);
        return "The username %s has been added successfully.".formatted(host.getUsername());

    }

    public static String removeAlbum(String username, String albumName) {

        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return "%s is not an artist.".formatted(username);
            }
        }

        for (Host host : hosts) {
            if (host.getUsername().equals(username)) {
                return "%s is not an artist.".formatted(username);
            }
        }

        for (Artist artist : artists) {
            if (artist.getUsername().equals(username)) {
                for(User user : users) {
                    String curr = user.lastLoaded;
                    if(user.getPlayer().getCurrentAudioFile() != null) {
                        Album currAlbum = artist.getAlbumByName(albumName);
                        if(currAlbum != null) {
                            if(currAlbum.getName().equals(user.getPlayer().getCurrentAudioFile().getName())) {
                                return "%s can't delete this album.".formatted(username);
                            }
                            for(Song song : currAlbum.getSongs()) {
                                if(song.getName().equals(user.getPlayer().getCurrentAudioFile().getName())) {
                                    return "%s can't delete this album.".formatted(username);
                                }
                            }
                        } else {
                            return "%s doesn't have an album with the given name.".formatted(username);
                        }
                        List <Playlist> playlists = user.getPlaylists();
                        for(Playlist playlist : playlists) {
                            if(curr.equals(playlist.getName())) {
                                for(Song song : playlist.getSongs()) {
                                    for(Song song1 : currAlbum.getSongs()) {
                                        if(song.getName().equals(song1.getName())) {
                                            return "%s can't delete this album.".formatted(username);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                for(User user1 : users) {
                    Iterator<Song> iterator = user1.getLikedSongs().iterator();
                    while (iterator.hasNext()) {
                        Song song = iterator.next();
                        if (song.getAlbum().equals(albumName)) {
                            iterator.remove();
                        }
                    }
                    List<Song> list = Admin.getSongs();
                    for(Song song : list) {
                        if(song.getAlbum().equals(albumName)) {
                            Admin.removeSong(song.getName());
                        }
                    }
                    List<Playlist> playlists = user1.getPlaylists();
                    for(Playlist playlist : playlists) {
                        Iterator<Song> iterator1 = playlist.getSongs().iterator();
                        while (iterator1.hasNext()) {
                            Song song = iterator1.next();
                            if (song.getAlbum().equals(albumName)) {
                                iterator1.remove();
                            }
                        }
                    }
                }
                for(Album album : artist.getAlbums()) {
                    if(album.getName().equals(albumName)) {
                        artist.getAlbums().remove(album);
                        return "%s deleted the album successfully.".formatted(username);
                    }
                }
            }
        }
        return "The username %s doesn't exist.".formatted(username);
    }

    public static String removePodcast(String username, String podcastName) {

        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return "%s is not a host.".formatted(username);
            }
        }

        for (Artist artist : artists) {
            if (artist.getUsername().equals(username)) {
                return "%s is not a host.".formatted(username);
            }
        }

        for (Host host : hosts) {
            if (host.getUsername().equals(username)) {
                for(User user : users) {
                    String curr = user.lastLoaded;
                    if(user.getPlayer().getCurrentAudioFile() != null) {
                        for(Podcast podcast : host.getPodcasts()) {
                            if(podcast.getName().equals(curr)) {
                                return "%s can't delete this podcast.".formatted(username);
                            }
                            for(Episode episode : podcast.getEpisodes()) {
                                if(episode.getName().equals(curr)) {
                                    return "%s can't delete this podcast.".formatted(username);
                                }
                            }
                        }
                    }
                }
                for(Podcast podcast : host.getPodcasts()) {
                    if(podcast.getName().equals(podcastName)) {
                        host.getPodcasts().remove(podcast);
                        return "%s deleted the podcast successfully.".formatted(username);
                    }
                }
                return "%s doesn't have a podcast with the given name.".formatted(username);
            }
        }
        return "The username %s doesn't exist.".formatted(username);
    }
}
