package sgtmelon.scriptum.office.intf;

import android.view.View;

public interface ItemIntf {

    interface Click {
        void onItemClick(View view, int p);
    }

    interface LongClick {
        void onItemLongClick(View view, int p);
    }

    interface Drag {
        void setItemDrag(boolean itemDrag);
    }

    interface Watcher {
        void onChanged(int p, String text);
    }

}
