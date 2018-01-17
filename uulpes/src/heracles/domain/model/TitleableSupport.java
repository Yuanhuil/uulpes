package heracles.domain.model;

public class TitleableSupport extends IdentifiableSupport implements Titleable {
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
