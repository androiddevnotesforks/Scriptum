package sgtmelon.handynotes.office;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.format.DateUtils;
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

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.app.data.DataRoom;
import sgtmelon.handynotes.app.data.DataInfo;
import sgtmelon.handynotes.office.def.DefColor;
import sgtmelon.handynotes.office.def.DefSort;
import sgtmelon.handynotes.office.def.data.DefCheck;
import sgtmelon.handynotes.office.def.data.DefType;
import sgtmelon.handynotes.app.model.item.ItemNote;
import sgtmelon.handynotes.app.model.item.ItemRoll;
import sgtmelon.handynotes.app.model.item.ItemSort;

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
                DataRoom db = DataRoom.provideDb(context);
                copyText = db.daoRoll().getText(itemNote.getCreate());
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

        public static int getColor(Context context, boolean isDark, int noteColor) {
            if (isDark) return ContextCompat.getColor(context, DefColor.dark[noteColor]);
            else return ContextCompat.getColor(context, DefColor.light[noteColor]);
        }

        public static int getColorLength() {
            return DefColor.light.length;
        }

        public static void tintMenuIcon(Context context, MenuItem item) {
            Drawable normalDrawable = item.getIcon();
            Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);
            DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(context, R.color.colorDark));

            item.setIcon(wrapDrawable);
        }

        public static Drawable getDrawable(Context context, @DrawableRes int drawableId) {
            Drawable drawable = ContextCompat.getDrawable(context, drawableId);
            if (drawable != null) {
                drawable.setColorFilter(ContextCompat.getColor(context, R.color.colorDark), PorterDuff.Mode.SRC_ATOP);
            }
            return drawable;
        }

        public static Drawable getDrawable(Context context, @DrawableRes int drawableId, @ColorRes int colorId) {
            Drawable drawable = ContextCompat.getDrawable(context, drawableId);
            if (drawable != null) {
                drawable.setColorFilter(ContextCompat.getColor(context, colorId), PorterDuff.Mode.SRC_ATOP);
            }
            return drawable;
        }

        public static Drawable getColorIcon(Context context, int position) {
            return ContextCompat.getDrawable(context, DefColor.icon[position]);
        }

        public static Drawable getColorCheck(Context context, int position) {
            return getDrawable(context, R.drawable.ic_button_color_check, DefColor.dark[position]);
        }

        public static int blendColors(int from, int to, float ratio) {
            final float inverseRatio = 1f - ratio;

            final float r = Color.red(to) * ratio + Color.red(from) * inverseRatio;
            final float g = Color.green(to) * ratio + Color.green(from) * inverseRatio;
            final float b = Color.blue(to) * ratio + Color.blue(from) * inverseRatio;

            return Color.rgb((int) r, (int) g, (int) b);
        }

        public static void tintButton(Context context, ImageButton button, @DrawableRes int drawableId, String text) {
            Drawable drawableEnable = getDrawable(context, drawableId);
            Drawable drawableDisable = getDrawable(context, drawableId, R.color.colorDarkSecond);

            if (!text.equals("")) {
                button.setImageDrawable(drawableEnable);
                button.setEnabled(true);
            } else {
                button.setImageDrawable(drawableDisable);
                button.setEnabled(false);
            }
        }

        public static void tintButton(Context context, ImageButton button, @DrawableRes int drawableId, String text, boolean enable) {
            Drawable drawableEnable = getDrawable(context, drawableId);
            Drawable drawableDisable = getDrawable(context, drawableId, R.color.colorDarkSecond);

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

        public static ItemNote fillCreate(Context context, @DefType int noteType) {
            ItemNote itemNote = new ItemNote();

            itemNote.setCreate(Time.getCurrentTime(context));
            itemNote.setName("");
            itemNote.setText("");

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            int ntColor = pref.getInt(context.getString(R.string.pref_key_color_create), context.getResources().getInteger(R.integer.pref_default_color_create));
            itemNote.setColor(ntColor);

            itemNote.setType(noteType);
            itemNote.setRankPs(new String[0]);
            itemNote.setRankId(new String[0]);
            itemNote.setBin(false);
            itemNote.setStatus(false);

            return itemNote;
        }

        public static String getCheckStr(@DefCheck int rollCheck, String rollAll) {
            return rollCheck == DefCheck.done ? rollAll + "/" + rollAll : "0/" + rollAll;
        }

        public static String getCheckStr(List<ItemRoll> listRoll) {
            return getCheckValue(listRoll) + DefCheck.divider + listRoll.size();
        }

        public static String getCheckStr(int rollCheck, int rollAll) {
            return rollCheck + DefCheck.divider + rollAll;
        }

        public static int getCheckValue(List<ItemRoll> listRoll) {
            int value = 0;
            for (ItemRoll itemRoll : listRoll) {
                if (itemRoll.isCheck()) value++;
            }
            return value;
        }

        //Получаем формат для отображения процентов (Например 15.6%)
        public static double getCheckValue(int rollCheck, int rollAll) {
            if (rollAll != 0) {
                if (rollCheck == rollAll) return 100.0;
                else return Math.floor(1000 * (double) rollCheck / (double) rollAll) / 10;
            }
            return 0.0;
        }

        public static boolean isAllCheck(List<ItemRoll> listRoll) {
            if (listRoll.size() != 0) {
                for (ItemRoll itemRoll : listRoll) {
                    if (!itemRoll.isCheck()) return false;
                }
                return true;
            } else return false;
        }

        public static String getName(Context context, String noteName) {
            if (!noteName.equals("")) return noteName;
            else return context.getString(R.string.hint_view_name);
        }

    }

    public static class Pref {

        //Формирование поискового запроса относительно настроек
        public static String getSortNoteOrder(Context context) {

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            String sortKeys = pref.getString(context.getString(R.string.pref_key_sort), getSortDefault());

            String[] sortKeysArr = sortKeys.split(DefSort.divider);
            StringBuilder order = new StringBuilder();

            for (String aSortKey : sortKeysArr) {
                @DefSort int key = Integer.parseInt(aSortKey);

                order.append(DataInfo.orders[key]);

                if (key != DefSort.create && key != DefSort.change) {
                    order.append(DefSort.divider);
                } else break;
            }

            return order.toString();
        }

        public static String getSortDefault() {
            return DefSort.create + DefSort.divider + DefSort.rank + DefSort.divider + DefSort.color;
        }

        public static String getSortByList(List<ItemSort> listSort) {
            StringBuilder sortKeys = new StringBuilder();
            for (int i = 0; i < listSort.size(); i++) {
                sortKeys.append(Integer.toString(listSort.get(i).getKey()));
                if (i != listSort.size() - 1) sortKeys.append(DefSort.divider);
            }
            return sortKeys.toString();
        }

        public static String getSortSummary(Context context, String sortKeys) {
            String sortSummary = "";
            String[] sortKeysArr = sortKeys.split(DefSort.divider);

            for (int k = 0; k < sortKeysArr.length; k++) {
                @DefSort int key = Integer.parseInt(sortKeysArr[k]);
                String summary = context.getResources().getStringArray(R.array.pref_text_sort)[key];

                if (k != 0) {
                    summary = summary.replace(context.getString(R.string.pref_summary_sort_start), "").replaceFirst(" ", "");
                }

                if (key != DefSort.create && key != DefSort.change) {
                    sortSummary += summary + DefSort.divider;
                } else {
                    sortSummary += summary;
                    break;
                }
            }
            return sortSummary;
        }

        public static void listAllPref(Context context, TextView textView) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

            textView.append("\n\nSort:");
            textView.append("\nNt: " + pref.getString(context.getString(R.string.pref_key_sort), Pref.getSortDefault()));

            textView.append("\n\nNotes:");
            textView.append("\nColorDef: " + pref.getInt(context.getString(R.string.pref_key_color_create), context.getResources().getInteger(R.integer.pref_default_color_create)));
            textView.append("\nPause:\t" + pref.getBoolean(context.getString(R.string.pref_key_pause_save), context.getResources().getBoolean(R.bool.pref_default_pause_save)));
            textView.append("\nSave:\t" + pref.getBoolean(context.getString(R.string.pref_key_auto_save), context.getResources().getBoolean(R.bool.pref_default_auto_save)));
            textView.append("\nSTime:\t" + pref.getInt(context.getString(R.string.pref_key_auto_save_time), context.getResources().getInteger(R.integer.pref_default_auto_save_time)));
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
