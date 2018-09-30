package sgtmelon.scriptum.office;

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
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.database.DbRoom;
import sgtmelon.scriptum.app.model.item.ItemNote;
import sgtmelon.scriptum.app.model.item.ItemRoll;
import sgtmelon.scriptum.app.model.item.ItemSort;
import sgtmelon.scriptum.office.annot.AnnColor;
import sgtmelon.scriptum.office.annot.def.DefSort;
import sgtmelon.scriptum.office.annot.def.DefTheme;
import sgtmelon.scriptum.office.annot.def.db.DefDb;
import sgtmelon.scriptum.office.annot.def.db.DefType;

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

    public static class Clr {

        public static int getNote(Context context, int color, boolean onDark) {
            switch (Pref.getTheme(context)) {
                case DefTheme.light:
                    return ContextCompat.getColor(context, AnnColor.cl_light[color]);
                case DefTheme.dark:
                default:
                    if (onDark) return ContextCompat.getColor(context, AnnColor.cl_dark[color]);
                    else return get(context, R.attr.clPrimary);
            }
        }

        public static int get(Context context, int color, boolean isDark) {
            if (isDark) return ContextCompat.getColor(context, AnnColor.cl_dark[color]);
            else return ContextCompat.getColor(context, AnnColor.cl_light[color]);
        }

        public static int get(Context context, @AttrRes int attr) {
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(attr, typedValue, true);
            return ContextCompat.getColor(context, typedValue.resourceId);
        }

        public static int getColorCheck(Context context, int position) {
            switch (Pref.getTheme(context)) {
                case DefTheme.light:
                    return ContextCompat.getColor(context, AnnColor.cl_dark[position]);
                default:
                case DefTheme.dark:
                    return ContextCompat.getColor(context, AnnColor.cl_light[position]);
            }
        }

        public static int blend(int from, int to, float ratio) {
            final float inverseRatio = 1f - ratio;

            final float r = Color.red(to) * ratio + Color.red(from) * inverseRatio;
            final float g = Color.green(to) * ratio + Color.green(from) * inverseRatio;
            final float b = Color.blue(to) * ratio + Color.blue(from) * inverseRatio;

            return Color.rgb((int) r, (int) g, (int) b);
        }

    }

    public static class Draw {

        public static Drawable get(Context context, @DrawableRes int drawableId, @AttrRes int attr) {
            Drawable drawable = ContextCompat.getDrawable(context, drawableId);

            int colorRes = Clr.get(context, attr);
            if (drawable != null) drawable.setColorFilter(colorRes, PorterDuff.Mode.SRC_ATOP);

            return drawable;
        }

        public static Drawable getIconColor(Context context, int position) {
            switch (Pref.getTheme(context)) {
                case DefTheme.light:
                    return ContextCompat.getDrawable(context, AnnColor.ic_light[position]);
                case DefTheme.dark:
                default:
                    return ContextCompat.getDrawable(context, AnnColor.ic_dark[position]);
            }
        }

    }

    public static class Tint {

        public static void menuIcon(Context context, MenuItem item) {
            Drawable normalDrawable = item.getIcon();
            Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);

            int colorRes = Clr.get(context, R.attr.clIcon);
            DrawableCompat.setTint(wrapDrawable, colorRes);

            item.setIcon(wrapDrawable);
        }

        public static void button(Context context, ImageButton button, @DrawableRes int drawableId, @AttrRes int clEnable, String text) {
            Drawable drawableEnable = Draw.get(context, drawableId, clEnable);
            Drawable drawableDisable = Draw.get(context, drawableId, R.attr.clSecond);

            if (!text.equals("")) {
                button.setImageDrawable(drawableEnable);
                button.setEnabled(true);
            } else {
                button.setImageDrawable(drawableDisable);
                button.setEnabled(false);
            }
        }

        public static void button(Context context, ImageButton button, @DrawableRes int drawableId, String text, boolean enable) {
            Drawable drawableEnable = Draw.get(context, drawableId, R.attr.clAccent);
            Drawable drawableDisable = Draw.get(context, drawableId, R.attr.clSecond);

            if (!text.equals("")) {
                if (enable) button.setImageDrawable(drawableEnable);
                else button.setImageDrawable(drawableDisable);

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

        public static boolean isFirstStart(Context context) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

            boolean val = pref.getBoolean(context.getString(R.string.pref_first_start), context.getResources().getBoolean(R.bool.pref_default_first_start));
            if (val) {
                pref.edit().putBoolean(context.getString(R.string.pref_first_start), false).apply();
            }

            return val;
        }

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

                order.append(DefDb.orders[key]);

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

        public static boolean getSortEqual(String keys1, String keys2) {
            String[] keysArr1 = keys1.split(DefSort.divider);
            String[] keysArr2 = keys2.split(DefSort.divider);

            for (int i = 0; i < keysArr1.length; i++) {
                if (!keysArr1[i].equals(keysArr2[i])) {
                    return false;
                }
                if (keysArr1[i].equals(Integer.toString(DefSort.create)) || keysArr1[i].equals(Integer.toString(DefSort.change))) {
                    break;
                }
            }

            return true;
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
