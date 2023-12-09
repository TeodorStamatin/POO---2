package app.searchBar;

import java.util.ArrayList;
import java.util.List;

public class FilterUtilsPage {

    private FilterUtilsPage() {
    }

    public static List<String> filterByName(final List<String> entries, final String name) {
        List<String> result = new ArrayList<>();
        for (String entry : entries) {
            if (entry.startsWith(name)) {
                result.add(entry);
            }
        }
        return result;
    }
}
