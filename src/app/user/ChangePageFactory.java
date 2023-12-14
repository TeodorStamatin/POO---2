package app.user;

class HomePage implements Page {
    @Override
    public String accessPage(final String username) {
        return "%s accessed Home successfully.".formatted(username);
    }
}

class LikedContentPage implements Page {
    @Override
    public String accessPage(final String username) {
        return "%s accessed LikedContent successfully.".formatted(username);
    }
}

public class ChangePageFactory {

    /**
     *
     * @param pageType
     * @param username
     * @return
     */
    public static Page createPage(final String pageType, final String username) {
        switch (pageType) {
            case "Home":
                return new HomePage();
            case "LikedContent":
                return new LikedContentPage();
            default:
                throw new IllegalArgumentException("%s is trying to access a non-existent page."
                        .formatted(username));
        }
    }
}
