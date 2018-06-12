package sgtmelon.handynotes;

import android.arch.persistence.room.Room;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import sgtmelon.handynotes.db.DbRoom;
import sgtmelon.handynotes.db.DbDesc;
import sgtmelon.handynotes.model.item.ItemNote;
import sgtmelon.handynotes.model.item.ItemRoll;
import sgtmelon.handynotes.model.item.ItemSort;

public class Help {

    //Копирование текста заметки в память
    public static void optionsCopy(Context context, ItemNote itemNote) {
        String copyText = "";

        if (!itemNote.getName().equals("")) {
            copyText = itemNote.getName() + "\n";   //Если есть название то добавляем его
        }

        switch (itemNote.getType()) {
            case DbDesc.typeText:
                copyText += itemNote.getText();     //В зависимости от типа составляем текст
                break;
            case DbDesc.typeRoll:
                DbRoom db = Room.databaseBuilder(context, DbRoom.class, "HandyNotes")
                        .allowMainThreadQueries()
                        .build();
                
                copyText = db.daoRoll().getText(itemNote.getCreate());

                db.close();
                break;
        }

        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE); //Сохраняем в память
        ClipData clip = ClipData.newPlainText("NoteText", copyText);

        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Текст скопирован в буфер обмена", Toast.LENGTH_SHORT).show();
        }
    }

    //Скрыть клавиатуру
    public static void hideKeyboard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (view != null && inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static class Array {

        public static String[] addStrItem(String[] strArr, String strItem) {
            int length = strArr.length;

            strArr = Arrays.copyOf(strArr, length + 1);
            strArr[length] = strItem;

            return strArr;
        }

        public static String[] strListToArr(List<String> list) {
            return list.toArray(new String[list.size()]);
        }

        public static List<String> strArrToList(String[] arr) {
            return new ArrayList<>(Arrays.asList(arr));
        }

    }

    public static class Icon {

        //Кружки для диалога смены цвета и фильтра
        private static final int[] colorIcon = new int[]{
                R.drawable.ic_button_color_00, R.drawable.ic_button_color_01,
                R.drawable.ic_button_color_02, R.drawable.ic_button_color_03,
                R.drawable.ic_button_color_04, R.drawable.ic_button_color_05,
                R.drawable.ic_button_color_06, R.drawable.ic_button_color_07,
                R.drawable.ic_button_color_08, R.drawable.ic_button_color_09,
                R.drawable.ic_button_color_10};

        public static final int[] colors = new int[]{
                R.color.noteRed, R.color.notePurple,
                R.color.noteIndigo, R.color.noteBlue,
                R.color.noteTeal, R.color.noteGreen,
                R.color.noteYellow, R.color.noteOrange,
                R.color.noteBrown, R.color.noteBlueGrey,
                R.color.noteWhite};

        public static final int[] colorsDark = new int[]{
                R.color.noteRedDark, R.color.notePurpleDark,
                R.color.noteIndigoDark, R.color.noteBlueDark,
                R.color.noteTealDark, R.color.noteGreenDark,
                R.color.noteYellowDark, R.color.noteOrangeDark,
                R.color.noteBrownDark, R.color.noteBlueGreyDark,
                R.color.noteWhiteDark};     //Цвета для заметок

        public static void tintMenuIcon(Context context, MenuItem item) {
            Drawable normalDrawable = item.getIcon();
            Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);
            DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(context, R.color.colorDark));

            item.setIcon(wrapDrawable);
        }

        public static Drawable getDrawable(Context context, @DrawableRes int drawableId) {
            Drawable drawable = ContextCompat.getDrawable(context, drawableId);
            drawable.setColorFilter(ContextCompat.getColor(context, R.color.colorDark), PorterDuff.Mode.SRC_ATOP);
            return drawable;
        }

        public static Drawable getDrawable(Context context, @DrawableRes int drawableId, @ColorRes int colorId) {
            Drawable drawable = ContextCompat.getDrawable(context, drawableId);
            drawable.setColorFilter(ContextCompat.getColor(context, colorId), PorterDuff.Mode.SRC_ATOP);
            return drawable;
        }

        public static Drawable[] getColorIconDrawable(Context context) {
            int iconCount = colorIcon.length;
            Drawable[] iconDrawable = new Drawable[iconCount];
            for (int i = 0; i < iconCount; i++) {
                iconDrawable[i] = ContextCompat.getDrawable(context, colorIcon[i]);
            }
            return iconDrawable;
        }

        public static Drawable[] getColorCheckDrawable(Context context) {
            int checkCount = colors.length;
            Drawable[] checkDrawable = new Drawable[checkCount];
            for (int i = 0; i < checkCount; i++) {
                checkDrawable[i] = getDrawable(context, R.drawable.ic_button_color_check, colorsDark[i]);
            }
            return checkDrawable;
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

        public static ItemNote fillCreate(Context context, int type) {
            ItemNote itemNote = new ItemNote();

            itemNote.setCreate(Time.getCurrentTime(context));
            itemNote.setName("");
            itemNote.setText("");

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            int ntColor = pref.getInt(context.getString(R.string.pref_key_color_create), context.getResources().getInteger(R.integer.pref_default_color_create));
            itemNote.setColor(ntColor);

            itemNote.setType(type);
            itemNote.setRankPs(new String[0]);
            itemNote.setRankId(new String[0]);
            itemNote.setBin(false);
            itemNote.setStatus(false);

            return itemNote;
        }

        public static final String dividerCheck = "/";

        public static String getCheckStr(int check, String checkMax) {
            String ntText;
            if (check == DbDesc.checkTrue) ntText = checkMax + "/" + checkMax;
            else ntText = "0/" + checkMax;
            return ntText;
        }

        public static String getCheckStr(List<ItemRoll> listRoll) {
            return getCheckValue(listRoll) + dividerCheck + listRoll.size();
        }

        public static String getCheckStr(int checkValue, int listRollSize) {
            return checkValue + dividerCheck + listRollSize;
        }

        public static int[] getCheckValue(String checkStr) {
            String[] value = checkStr.split(dividerCheck);
            return new int[]{Integer.parseInt(value[0]), Integer.parseInt(value[1])};
        }

        public static int getCheckValue(List<ItemRoll> listRoll) {
            int value = 0;
            for (ItemRoll itemRoll : listRoll)
                if (itemRoll.isCheck()) value++;
            return value;
        }

        //Получаем формат для отображения процентов (Например 15.6%)
        public static double getCheckValue(int rollCheckCount, int rollAllCount) {
            if (rollAllCount != 0) {
                if (rollCheckCount == rollAllCount) return 100.0;
                else return Math.floor(1000 * (double) rollCheckCount / (double) rollAllCount) / 10;
            }
            return 0.0;
        }

        public static boolean isAllCheck(List<ItemRoll> listRoll) {
            if (listRoll.size() == 0) return false;
            else {
                for (ItemRoll itemRoll : listRoll) {
                    if (!itemRoll.isCheck()) return false;
                }
                return true;
            }
        }

        public static String getName(Context context, String noteName) {
            if (!noteName.equals("")) return noteName;
            else return context.getString(R.string.hint_view_name);
        }
    }

    public static class Pref {

        public static final int sortCr = 0, sortCh = 1;
        private static final int sortRk = 2, sortCl = 3;

        public static final String divider = ", ";

        //Формирование поискового запроса относительно настроек
        public static String getSortNoteOrder(Context context) {

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            String sortKeys = pref.getString(context.getString(R.string.pref_key_sort), getSortDefault());

            String[] sortKeysArr = sortKeys.split(Help.Pref.divider);
            StringBuilder order = new StringBuilder();

            for (String aSortKey : sortKeysArr) {
                int key = Integer.parseInt(aSortKey);

                order.append(DbDesc.orders[key]);

                if (key != Help.Pref.sortCr && key != Help.Pref.sortCh) {
                    order.append(Help.Pref.divider);
                } else break;
            }

            return order.toString();
        }

        public static String getSortDefault() {
            return sortCr + divider + sortRk + divider + sortCl;
        }

        public static String getSortByList(List<ItemSort> listSort) {
            StringBuilder sortKeys = new StringBuilder();
            for (int i = 0; i < listSort.size(); i++) {
                sortKeys.append(Integer.toString(listSort.get(i).getKey()));
                if (i != listSort.size() - 1) sortKeys.append(divider);
            }
            return sortKeys.toString();
        }

        public static String getSortSummary(Context context, String sortKeys) {
            String sortSummary = "";
            String[] sortKeysArr = sortKeys.split(divider);

            for (int k = 0; k < sortKeysArr.length; k++) {
                int key = Integer.parseInt(sortKeysArr[k]);
                String summary = context.getResources().getStringArray(R.array.pref_text_sort)[key];
                if (k != 0)
                    summary = summary.replace(context.getString(R.string.pref_summary_sort_start), "").replaceFirst(" ", "");

                if (key != sortCr && key != sortCh) sortSummary += summary + divider;
                else {
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
