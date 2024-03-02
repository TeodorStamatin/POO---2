package app.utils;


public class Event {
    private String name;
    private String description;
    private String date;

    public Event() {
    }

    public Event(final String name, final String date, final String description) {
        this.name = name;
        this.description = description;
        this.date = date;
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

    /**
     *
     * @return
     */
    public String getDate() {
        return date;
    }

}
