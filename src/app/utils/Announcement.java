package app.utils;

import lombok.Getter;

public class Announcement {

    @Getter
    private String name;
    @Getter
    private String description;

    public Announcement() {
    }

    public Announcement(final String name, final String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
