package app.utils;

public class Merch {
    private String name;
    private String description;
    private Integer price;

    public Merch() {
    }

    public Merch(final String name, final String description, final Integer price) {
        this.name = name;
        this.description = description;
        this.price = price;
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
    public Integer getPrice() {
        return price;
    }
}
