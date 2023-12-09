package app.utils;

import lombok.Getter;

public class Merch {
    @Getter
    private String name;
    @Getter
    private String description;
    @Getter
    private Integer price;

    public Merch() {
    }

    public Merch(final String name, final String description, final Integer price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getPrice() {
        return price;
    }
}
