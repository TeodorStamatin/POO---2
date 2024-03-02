package app.utils;

public class Announcement {

    private String name;
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
