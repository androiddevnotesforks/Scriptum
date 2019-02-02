package sgtmelon.scriptum.office.intf;

import android.view.View;

import androidx.annotation.NonNull;

public interface ItemIntf {

    interface ClickListener {
        void onItemClick(@NonNull View view, int p);
    }

    interface LongClickListener {
        boolean onItemLongClick(@NonNull View view, int p);
    }

    interface DragListener {
        void setDrag(boolean drag);
    }

    interface RollWatcher {
        void afterRollChanged(int p, @NonNull String text);
    }

}