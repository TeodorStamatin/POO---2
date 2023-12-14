package app.utils;

import lombok.Getter;

public class Announcement {

    @Getter
    private String name;
    @Getter
    private String description;

    public Announcement() {
    }

    /**
     *
     * @param name
     * @param description
     */
    public Announcement(final String name, final String description) {
        this.name = name;
        this.description = description;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

}
