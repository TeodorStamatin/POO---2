package app.user;

import app.Admin;
import app.utils.Announcement;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.audio.Collections.Album;
import app.utils.Merch;
import app.utils.Event;
import lombok.Getter;
import app.audio.Files.Song;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Artist {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public List<Merch> merch = new ArrayList<>();
    public List<Event> events = new ArrayList<>();
    @Getter
    private String username;
    @Getter
    private int age;
    @Getter
    private String city;
    @Getter
    private List<Album> albums;

    public Artist(final String username, final int age, final String city) {
        this.username = username;
        this.age = age;
        this.city = city;
        this.albums = new ArrayList<>();
    }

    public List<Event> getEvents() {
        return events;
    }

    public List<Merch> getMerch() {
        return merch;
    }

    public void addAlbum(final Album album) {
        this.albums.add(album);
    }

    public boolean albumExists(final String albumName) {
        for (Album album : this.albums) {
            if (album.getName().equals(albumName)) {
                return true;
            }
        }
        return false;
    }

    public List<ObjectNode> showAlbums(final String username) {
        List<ObjectNode> results = new ArrayList<>();
        for (Artist artist : Admin.getArtists()) {
            if (artist.getUsername().equals(username)) {
                for(Album album : artist.getAlbums()) {
                    ObjectNode albumNode = objectMapper.createObjectNode();
                    albumNode.put("name", album.getName());
                    List<String> songs = new ArrayList<>();
                    for(Song song : album.getSongs()) {
                        songs.add(song.getName());
                    }
                    albumNode.putPOJO("songs", songs);
                    results.add(albumNode);
                }
            }
        }
        return results;
    }

    public Album getAlbumByName(final String albumName) {
        for (Album album : this.albums) {
            if (album.getName().equals(albumName)) {
                return album;
            }
        }
        return null;
    }

    public String addEvent(final String name, final String date, final String description) {
        if(!validDate(date)) {
            return "Event for %s does not have a valid date.".formatted(this.username);
        }

        for (Event event : this.getEvents()) {
            if (event.getName().equals(name)) {
                return "%s has another event with the same name.".formatted(this.username);
            }
        }

        Event event = new Event(name, date, description);
        this.events.add(event);
        return "%s has added new event successfully.".formatted(this.username);
    }

    private boolean validDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        dateFormat.setLenient(false);  // Disable lenient parsing to enforce strict validation

        try {
            Date parsedDate = dateFormat.parse(date);

            // Additional checks for day, month, and year
            int day = Integer.parseInt(date.substring(0, 2));
            int month = Integer.parseInt(date.substring(3, 5));
            int year = Integer.parseInt(date.substring(6));

            if (day < 1 || day > 31 || month < 1 || month > 12 || year < 1900 || year > 2023) {
                return false;
            }

            // Check for February
            if (month == 2 && day > 28) {
                return false;
            }

            return true;
        } catch (ParseException | NumberFormatException e) {
            // Parsing failed or conversion to integer failed
            return false;
        }
    }

    public String removeEvent(final String name) {
        for (Event event : this.getEvents()) {
            if (event.getName().equals(name)) {
                this.events.remove(event);
                return "%s deleted the event successfully.".formatted(this.username);
            }
        }
        return "%s doesn't have an event with the given name.".formatted(this.username);
    }

    public String addMerch(final String name, final Integer price, final String description) {
        if(price < 0) {
            return "Price for merchandise can not be negative.";
        }

        for (Merch merch : this.getMerch()) {
            if (merch.getName().equals(name)) {
                return "%s has merchandise with the same name.".formatted(this.username);
            }
        }

        Merch merch = new Merch(name, description, price);
        this.merch.add(merch);
        return "%s has added new merchandise successfully.".formatted(this.username);
    }
}
