package app.searchBar;

import fileio.input.FiltersInput;
import lombok.Data;


@Data
public class FiltersPage {
    private String name;

    /**
     *
     * @param filters
     */
    public FiltersPage(final FiltersInput filters) {
        this.name = filters.getName();
    }
}
