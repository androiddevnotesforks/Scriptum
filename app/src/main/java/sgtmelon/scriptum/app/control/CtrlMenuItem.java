package sgtmelon.scriptum.app.control;

import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

// TODO: 26.09.2018 think about it

public class CtrlMenuItem {

    private final List<MenuItem> listItem = new ArrayList<>();

    public void add(MenuItem menuItem) {
        listItem.add(menuItem);
    }

    public void setVisible(int position, boolean visible) {

    }

}
