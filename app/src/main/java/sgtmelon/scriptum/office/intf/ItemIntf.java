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
        void setDrag(boolean drag);
    }

    interface RollWatcher {
        void afterRollChanged(int p, String text);
    }

}
