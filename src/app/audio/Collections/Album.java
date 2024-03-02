package app.audio.Collections;


import app.audio.Files.AudioFile;
import lombok.Getter;
import app.audio.Files.Song;
import java.util.List;
import java.util.ArrayList;

public class Album extends AudioCollection {
    @Getter
    private int releaseYear;
    @Getter
    private String description;
    private List<Song> songs;

    public Album(final String name, final int releaseYear,
                 final String description, final String owner) {
        super(name, owner);
        this.releaseYear = releaseYear;
        this.description = description;
        this.songs = new ArrayList<>();
    }

    /**
     *
     * @param song
     */
    public void addSong(final Song song) {
        this.songs.add(song);
    }

    /**
     *
     * @param songName
     * @return
     */
    public boolean songExists(final String songName) {
        for (Song song : this.songs) {
            if (song.getName().equals(songName)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return
     */
    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }

    /**
     *
     * @param index the index
     * @return
     */
    @Override
    public AudioFile getTrackByIndex(final int index) {
        return songs.get(index);
    }

    /**
     *
     * @return
     */
    public List<Song> getSongs() {
        return songs;
    }

}
