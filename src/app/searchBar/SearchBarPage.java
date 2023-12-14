package app.searchBar;

import app.Admin;
import app.user.Artist;
import app.user.Host;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static app.searchBar.FilterUtilsPage.filterByName;

public final class SearchBarPage {
    private List<String> results;
    private final String user;
    private static final Integer MAX_RESULTS = 5;
    @Getter
    private String lastSearchType;
    @Getter
    private String lastSelected;

    /**
     *
     * @param user
     */
    public SearchBarPage(final String user) {
        this.results = new ArrayList<>();
        this.user = user;
    }

    /**
     *
     * @param filters
     * @param type
     * @return
     */
    public List<String> search(final Filters filters, final String type) {
        List<String> entries;

        switch (type) {
            case "artist":
                List<Artist> tmp1 = new ArrayList<>(Admin.getArtists());
                entries = new ArrayList<>();

                for (Artist artist : tmp1) {
                    entries.add(artist.getUsername());
                }

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }
                break;
            case "host":
                List<Host> tmp2 = new ArrayList<>(Admin.getHosts());
                entries = new ArrayList<>();

                for (Host host : tmp2) {
                    entries.add(host.getUsername());
                }

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                break;
            default:
                entries = new ArrayList<>();
                break;
        }

        while (entries.size() > MAX_RESULTS) {
            entries.remove(entries.size() - 1);
        }

        this.results = entries;
        this.lastSearchType = type;
        return this.results;
    }

    /**
     *
     * @param itemNumber
     * @return
     */
    public String select(final Integer itemNumber) {
        if (this.results.size() < itemNumber) {
            results.clear();

            return null;
        } else {
            lastSelected =  this.results.get(itemNumber - 1);
            results.clear();

            return lastSelected;
        }
    }
}
