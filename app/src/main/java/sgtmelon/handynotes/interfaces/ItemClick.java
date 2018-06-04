package sgtmelon.handynotes.interfaces;

import android.view.View;

public interface ItemClick {

    interface Click {
        void onItemClick(View view, int p);
    }

    interface LongClick {
        void onItemLongClick(View view, int p);
    }

    interface Drag {
        void setDrag(boolean itemDrag);
    }
}
