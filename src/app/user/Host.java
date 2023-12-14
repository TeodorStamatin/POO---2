package app.user;

import app.Admin;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.utils.Announcement;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Host {
    private static ObjectMapper objectMapper = new ObjectMapper();
    @Getter
    private List<Announcement> announcements = new ArrayList<>();
    @Getter
    private List<Podcast> podcasts;
    @Getter
    private String username;
    @Getter
    private int age;
    @Getter
    private String city;

    public Host(final String username, final int age, final String city) {
        this.username = username;
        this.age = age;
        this.city = city;
        this.podcasts = new ArrayList<>();
    }

    /**
     *
     * @param podcastName
     * @return
     */
    public boolean podcastExists(final String podcastName) {
        for (Podcast podcast : podcasts) {
            if (podcast.getName().equals(podcastName)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param name
     * @param description
     * @return
     */
    public String addAnnouncement(final String name, final String description) {

        for (Announcement announcement : this.getAnnouncements()) {
            if (announcement.getName().equals(name)) {
                return "%s has another announcement with the same name.".formatted(this.username);
            }
        }

        Announcement announcement = new Announcement(name, description);
        this.announcements.add(announcement);
        return "%s has successfully added new announcement.".formatted(this.username);
    }

    /**
     *
     * @param name
     * @return
     */
    public String removeAnnouncement(final String name) {
        for (Announcement announcement : this.getAnnouncements()) {
            if (announcement.getName().equals(name)) {
                this.announcements.remove(announcement);
                return "%s has successfully deleted the announcement.".formatted(this.username);
            }
        }
        return "%s has no announcement with the given name.".formatted(this.username);
    }

    /**
     *
     * @param username
     * @return
     */
    public List<ObjectNode> showPodcasts(final String username) {
        List<ObjectNode> results = new ArrayList<>();
        for (Host host : Admin.getHosts()) {
            if (host.getUsername().equals(username)) {
                for (Podcast podcast : host.getPodcasts()) {
                    ObjectNode albumNode = objectMapper.createObjectNode();
                    albumNode.put("name", podcast.getName());
                    List<String> episodes = new ArrayList<>();
                    for (Episode episode : podcast.getEpisodes()) {
                        episodes.add(episode.getName());
                    }
                    albumNode.putPOJO("episodes", episodes);
                    results.add(albumNode);
                }
            }
        }
        return results;
    }
}
