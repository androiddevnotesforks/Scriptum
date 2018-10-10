package sgtmelon.handynotes.app.model.item;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

public class ItemIntro {

    private int icon, title, details;

    public ItemIntro(@DrawableRes int icon, @StringRes int title, @StringRes int details) {
        this.icon = icon;
        this.title = title;
        this.details = details;
    }

    public int getIcon() {
        return icon;
    }

    public int getTitle() {
        return title;
    }

    public int getDetails() {
        return details;
    }

}
