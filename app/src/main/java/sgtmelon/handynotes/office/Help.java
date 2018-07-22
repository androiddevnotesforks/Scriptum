package sgtmelon.handynotes.office;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.AttrRes;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import sgtmelon.handynotes.R;
import sgtmelon.handynotes.app.dataBase.DbRoom;
import sgtmelon.handynotes.app.model.item.ItemNote;
import sgtmelon.handynotes.app.model.item.ItemRoll;
import sgtmelon.handynotes.app.model.item.ItemSort;
import sgtmelon.handynotes.office.annot.Db;
import sgtmelon.handynotes.office.annot.def.DefColor;
import sgtmelon.handynotes.office.annot.def.DefSort;
import sgtmelon.handynotes.office.annot.def.DefTheme;
import sgtmelon.handynotes.office.annot.def.db.DefType;

public class Help {

    //Копирование текста заметки в память
    public static void optionsCopy(Context context, ItemNote itemNote) {
        String copyText = "";

        if (!itemNote.getName().equals("")) {
            copyText = itemNote.getName() + "\n";   //Если есть название то добавляем его
        }

        switch (itemNote.getType()) {
            case DefType.text:
                copyText += itemNote.getText();     //В зависимости от типа составляем текст
                break;
            case DefType.roll:
                DbRoom db = DbRoom.provideDb(context);
                copyText = db.daoRoll().getText(itemNote.getId());
                db.close();
                break;
        }

        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE); //Сохраняем в память
        ClipData clip = ClipData.newPlainText("NoteText", copyText);

        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, context.getString(R.string.toast_text_copy), Toast.LENGTH_SHORT).show();
        }
    }

    //Скрыть клавиатуру
    public static void hideKeyboard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (view != null && inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static class Icon {

        public static int getColorNote(Context context, int color, boolean onDark) {
            switch (Pref.getTheme(context)) {
                case DefTheme.light:
                    return ContextCompat.getColor(context, DefColor.light[color]);
                default:
                    if (onDark) return ContextCompat.getColor(context, DefColor.light[color]);
                    else return getColor(context, R.attr.clPrimary);
            }
        }

        public static int getColor(Context context, boolean isDark, int color) {
            if (isDark) return ContextCompat.getColor(context, DefColor.dark[color]);
            else return ContextCompat.getColor(context, DefColor.light[color]);
        }

        public static int getColorLength() {
            return DefColor.light.length;
        }

        public static int getColor(Context context, @AttrRes int attr, boolean isAccent) {
            if (isAccent) return ContextCompat.getColor(context, R.color.iconAccent);
            else {
                TypedValue typedValue = new TypedValue();
                context.getTheme().resolveAttribute(attr, typedValue, true);

                return ContextCompat.getColor(context, typedValue.resourceId);
            }
        }

        public static int getColor(Context context, @AttrRes int attr) {
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(attr, typedValue, true);
            return ContextCompat.getColor(context, typedValue.resourceId);
        }

        public static void tintMenuIcon(Context context, MenuItem item) {
            Drawable normalDrawable = item.getIcon();
            Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);

            int colorRes = getColor(context, R.attr.clIcon);
            DrawableCompat.setTint(wrapDrawable, colorRes);

            item.setIcon(wrapDrawable);
        }

        public static Drawable getDrawable(Context context, @AttrRes int attr, @DrawableRes int drawableId) {
            Drawable drawable = ContextCompat.getDrawable(context, drawableId);
            if (drawable != null) {
                int colorRes = getColor(context, attr);
                drawable.setColorFilter(colorRes, PorterDuff.Mode.SRC_ATOP);
            }
            return drawable;
        }

        public static Drawable getColorIcon(Context context, int position) {
            return ContextCompat.getDrawable(context, DefColor.icon[position]);
        }

        public static Drawable getColorCheck(Context context, int position) {
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_color_check);
            if (drawable != null) {
                int colorRes = ContextCompat.getColor(context, DefColor.dark[position]);
                drawable.setColorFilter(colorRes, PorterDuff.Mode.SRC_ATOP);
            }
            return drawable;
        }

        public static int blendColors(int from, int to, float ratio) {
            final float inverseRatio = 1f - ratio;

            final float r = Color.red(to) * ratio + Color.red(from) * inverseRatio;
            final float g = Color.green(to) * ratio + Color.green(from) * inverseRatio;
            final float b = Color.blue(to) * ratio + Color.blue(from) * inverseRatio;

            return Color.rgb((int) r, (int) g, (int) b);
        }

        public static void tintButton(Context context, ImageButton button, @DrawableRes int drawableId, @AttrRes int attr, String text) {
            Drawable drawableEnable = getDrawable(context, attr, drawableId);
            Drawable drawableDisable = getDrawable(context, R.attr.clSecond, drawableId);

            if (!text.equals("")) {
                button.setImageDrawable(drawableEnable);
                button.setEnabled(true);
            } else {
                button.setImageDrawable(drawableDisable);
                button.setEnabled(false);
            }
        }

        public static void tintButton(Context context, ImageButton button, @DrawableRes int drawableId, String text, boolean enable) {
            Drawable drawableEnable = getDrawable(context, R.attr.clAccent, drawableId);
            Drawable drawableDisable = getDrawable(context, R.attr.clSecond, drawableId);

            if (!text.equals("")) {
                if (enable)
                    button.setImageDrawable(drawableEnable);
                else
                    button.setImageDrawable(drawableDisable);
                button.setEnabled(enable);
            } else {
                button.setImageDrawable(drawableDisable);
                button.setEnabled(false);
            }
        }
    }

    public static class Note {

        /**
         * @param listRoll - Список для проверки
         * @return - Количество отмеченных пунктов
         */
        public static int getRollCheck(List<ItemRoll> listRoll) {
            int rollCheck = 0;
            for (ItemRoll itemRoll : listRoll) {
                if (itemRoll.isCheck()) rollCheck++;
            }
            return rollCheck;
        }

        /**
         * @param rollCheck - Количество выполненных пунктов
         * @param rollAll   - Всего пунктов
         * @return - Получаем формат для отображения процентов (Например 15.6%)
         */
        public static double getRollCheck(int rollCheck, int rollAll) {
            double value = 0.0;
            if (rollAll != 0) {
                if (rollCheck == rollAll) value = 100.0;
                else value = Math.floor(1000 * (double) rollCheck / (double) rollAll) / 10;
            }
            return value;
        }

        /**
         * @param listRoll - Список для проверки
         * @return - Все ли пункты отмечены
         */
        public static boolean isAllCheck(List<ItemRoll> listRoll) {
            if (listRoll.size() != 0) {
                for (ItemRoll itemRoll : listRoll) {
                    if (!itemRoll.isCheck()) return false;
                }
                return true;
            } else return false;
        }

    }

    public static class Pref {

        /**
         * @param context - Для получения настроек
         * @return - Формирование поискового запроса относительно настроек
         */
        public static String getSortNoteOrder(Context context) {

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

            String keysStr = pref.getString(context.getString(R.string.pref_key_sort), DefSort.def);
            String[] keysArr = keysStr.split(DefSort.divider);

            StringBuilder order = new StringBuilder();
            for (String aKey : keysArr) {
                @DefSort int key = Integer.parseInt(aKey);

                order.append(Db.orders[key]);

                if (key != DefSort.create && key != DefSort.change) {
                    order.append(DefSort.divider);
                } else break;
            }

            return order.toString();
        }

        /**
         * @param listSort - Список моделей из диалога
         * @return - Строка сортировки
         */
        public static String getSortByList(List<ItemSort> listSort) {
            StringBuilder order = new StringBuilder();
            for (int i = 0; i < listSort.size(); i++) {
                order.append(Integer.toString(listSort.get(i).getKey()));

                if (i != listSort.size() - 1) {
                    order.append(DefSort.divider);
                }
            }
            return order.toString();
        }

        public static String getSortSummary(Context context, String keys) {
            StringBuilder order = new StringBuilder();

            String[] keysArr = keys.split(DefSort.divider);
            String[] keysName = context.getResources().getStringArray(R.array.pref_text_sort);

            for (int k = 0; k < keysArr.length; k++) {
                @DefSort int key = Integer.parseInt(keysArr[k]);

                String summary = keysName[key];
                if (k != 0) {
                    summary = summary.replace(context.getString(R.string.pref_summary_sort_start), "").replaceFirst(" ", "");
                }

                order.append(summary);
                if (key != DefSort.create && key != DefSort.change) {
                    order.append(DefSort.divider);
                } else break;

            }

            return order.toString();
        }

        public static void listAllPref(Context context, TextView textView) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

            textView.append("\n\nSort:");
            textView.append("\nNt: " + pref.getString(context.getString(R.string.pref_key_sort), DefSort.def));

            textView.append("\n\nNotes:");
            textView.append("\nColorDef: " + pref.getInt(context.getString(R.string.pref_key_color), context.getResources().getInteger(R.integer.pref_default_color)));
            textView.append("\nPause:\t" + pref.getBoolean(context.getString(R.string.pref_key_pause_save), context.getResources().getBoolean(R.bool.pref_default_pause_save)));
            textView.append("\nSave:\t" + pref.getBoolean(context.getString(R.string.pref_key_auto_save), context.getResources().getBoolean(R.bool.pref_default_auto_save)));
            textView.append("\nSTime:\t" + pref.getInt(context.getString(R.string.pref_key_save_time), context.getResources().getInteger(R.integer.pref_default_save_time)));
            textView.append("\nTheme:\t" + pref.getInt(context.getString(R.string.pref_key_theme), context.getResources().getInteger(R.integer.pref_default_theme)));
        }

        public static int getTheme(Context context) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            return pref.getInt(context.getString(R.string.pref_key_theme), context.getResources().getInteger(R.integer.pref_default_theme));
        }
    }

    public static class Time {

        //Возвращает текущее время в нужном формате
        public static String getCurrentTime(Context context) {
            return new SimpleDateFormat(context.getString(R.string.date_app_format), Locale.getDefault()).format(Calendar.getInstance().getTime());
        }

        //Преобразует дату в приятный вид
        public static String formatNoteDate(Context context, String date) {
            DateFormat oldDateFormat = new SimpleDateFormat(context.getString(R.string.date_app_format), Locale.getDefault()), newDateFormat;
            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime(oldDateFormat.parse(date));
                if (DateUtils.isToday(calendar.getTimeInMillis()))
                    newDateFormat = new SimpleDateFormat(context.getString(R.string.date_note_today_format), Locale.getDefault());
                else
                    newDateFormat = new SimpleDateFormat(context.getString(R.string.date_note_yesterday_format), Locale.getDefault());
                return newDateFormat.format(oldDateFormat.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

}
