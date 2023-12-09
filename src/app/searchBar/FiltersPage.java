package app.searchBar;

import fileio.input.FiltersInput;
import lombok.Data;

import java.util.ArrayList;

@Data
public class FiltersPage {
    private String name;

    public FiltersPage(final FiltersInput filters) {
        this.name = filters.getName();
    }
}
