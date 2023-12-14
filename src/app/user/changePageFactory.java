package app.user;

class HomePage implements Page {
    @Override
    public String accessPage(String username) {
        return "%s accessed Home successfully.".formatted(username);
    }
}

class LikedContentPage implements Page {
    @Override
    public String accessPage(String username) {
        return "%s accessed LikedContent successfully.".formatted(username);
    }
}

public class changePageFactory {
    public static Page createPage(String pageType, String username) {
        switch (pageType) {
            case "Home":
                return new HomePage();
            case "LikedContent":
                return new LikedContentPage();
            default:
                throw new IllegalArgumentException("%s is trying to access a non-existent page.".formatted(username));
        }
    }
}