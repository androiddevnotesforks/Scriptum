package sgtmelon.scriptum.office.intf;

import android.view.View;

public interface ItemIntf {

    interface ClickListener {
        void onItemClick(View view, int p);
    }

    interface LongClickListener {
        void onItemLongClick(View view, int p);
    }

    interface DragListener {
        void setItemDrag(boolean itemDrag);
    }

    interface Watcher {
        void onChanged(int p, String text);
    }

}
