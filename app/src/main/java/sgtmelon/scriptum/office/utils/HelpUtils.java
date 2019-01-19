package sgtmelon.scriptum.office.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.model.item.RollItem;
import sgtmelon.scriptum.office.annot.def.TypeNoteDef;

public final class HelpUtils {

    /**
     * Копирование текста заметки в память
     *
     * @param noteItem - Заметка для копирования
     */
    public static void optionsCopy(@NonNull Context context, @NonNull NoteItem noteItem) {
        String copyText = "";

        if (!TextUtils.isEmpty(noteItem.getName())) {
            copyText = noteItem.getName() + "\n";   //Если есть название то добавляем его
        }

        switch (noteItem.getType()) {
            case TypeNoteDef.text:
                copyText += noteItem.getText();     //В зависимости от типа составляем текст
                break;
            case TypeNoteDef.roll:
                final RoomDb db = RoomDb.provideDb(context);
                copyText = db.daoRoll().getText(noteItem.getId());
                db.close();
                break;
        }

        //Сохраняем данные в память
        final ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        final ClipData clip = ClipData.newPlainText("NoteText", copyText); // TODO: 02.11.2018 вынеси

        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, context.getString(R.string.toast_text_copy), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Скрыть клавиатуру
     *
     * @param view - Текущий фокус
     */
    public static void hideKeyboard(@NonNull Context context, @Nullable View view) {
        final InputMethodManager inputMethodManager = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (view != null && inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static final class Note {

        /**
         * @param listRoll - Список для проверки
         * @return - Количество отмеченных пунктов
         */
        public static int getRollCheck(@NonNull List<RollItem> listRoll) {
            int rollCheck = 0;

            for (final RollItem rollItem : listRoll) {
                if (rollItem.isCheck()) {
                    rollCheck++;
                }
            }

            return rollCheck;
        }

        /**
         * @param listRoll - Список для проверки
         * @return - Все ли пункты отмечены
         */
        public static boolean isAllCheck(@NonNull List<RollItem> listRoll) {
            if (listRoll.size() == 0) return false;

            for (final RollItem rollItem : listRoll) {
                if (!rollItem.isCheck()) {
                    return false;
                }
            }

            return true;
        }

    }

}