package sgtmelon.scriptum.office.intf;

import android.view.View;

import androidx.annotation.Nullable;

public interface IntfItem {

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

    interface Animation {
        void setDrawable(boolean drawableOn, boolean needAnim);
    }

}
