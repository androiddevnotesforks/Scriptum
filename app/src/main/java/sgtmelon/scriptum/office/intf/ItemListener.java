package sgtmelon.scriptum.office.intf;

import android.view.View;

import androidx.annotation.NonNull;

/**
 * Интерфейс для обработки нажатий на элементы
 * На Java, чтобы использовать lambda в Kotlin
 */
public interface ItemListener {

    interface ClickListener {
        void onItemClick(@NonNull View view, int p);
    }

    interface LongClickListener {
        void onItemLongClick(@NonNull View view, int p);
    }

    interface DragListener {
        void setDrag(boolean drag);
    }

}