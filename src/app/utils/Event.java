package app.utils;

import lombok.Getter;

public class Event {
    @Getter
    private String name;
    @Getter
    private String description;
    @Getter
    private String date;

    public Event() {
    }

    public Event(final String name, final String date, final String description) {
        this.name = name;
        this.description = description;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

}
