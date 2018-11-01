package sgtmelon.scriptum.office;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.text.TextUtils;
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
import androidx.annotation.FloatRange;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.model.item.RollItem;
import sgtmelon.scriptum.app.model.item.SortItem;
import sgtmelon.scriptum.office.annot.ColorAnn;
import sgtmelon.scriptum.office.annot.DbAnn;
import sgtmelon.scriptum.office.annot.def.SortDef;
import sgtmelon.scriptum.office.annot.def.ThemeDef;
import sgtmelon.scriptum.office.annot.def.TypeDef;

public final class Help {

    /**
     * Копирование текста заметки в память
     *
     * @param noteItem - Заметка для копирования
     */
    public static void optionsCopy(Context context, NoteItem noteItem) {
        String copyText = "";

        if (!TextUtils.isEmpty(noteItem.getName())) {
            copyText = noteItem.getName() + "\n";   //Если есть название то добавляем его
        }

        switch (noteItem.getType()) {
            case TypeDef.text:
                copyText += noteItem.getText();     //В зависимости от типа составляем текст
                break;
            case TypeDef.roll:
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
    public static void hideKeyboard(Context context, View view) {
        final InputMethodManager inputMethodManager = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (view != null && inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static final class Clr {

        /**
         * Получение цвета заметки в зависимости от темы и заднего фона
         *
         * @param color  - Идентификатор цвета заметки
         * @param onDark - Если элемент находится на тёмном фоне (например индикатор цвета заметки
         * @return - Один из стандартных цветов приложения
         */
        public static int getNote(Context context, int color, boolean onDark) {
            switch (Pref.getTheme(context)) {
                case ThemeDef.light:
                    return ContextCompat.getColor(context, ColorAnn.cl_light[color]);
                case ThemeDef.dark:
                default:
                    return onDark
                            ? ContextCompat.getColor(context, ColorAnn.cl_dark[color])
                            : get(context, R.attr.clPrimary);
            }
        }

        /**
         * Получение одного из стандартных цветов заметки в зави
         *
         * @param color     - Идентификатор цвета заметки
         * @param colorDark - Тёмный цвет или нет
         * @return - Один из стандартных цветов приложения
         */
        public static int get(Context context, int color, boolean colorDark) {
            return colorDark
                    ? ContextCompat.getColor(context, ColorAnn.cl_dark[color])
                    : ContextCompat.getColor(context, ColorAnn.cl_light[color]);
        }

        /**
         * Получение цвета по аттрибуту
         *
         * @param attr - Аттрибут цвета
         * @return - Цвет в записимости от отрибута
         */
        public static int get(Context context, @AttrRes int attr) {
            final TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(attr, typedValue, true);
            return ContextCompat.getColor(context, typedValue.resourceId);
        }

        /**
         * Получение RGB промежуточного цвета в записимости от значения трансформации
         *
         * @param from  - Цвет, от которого происходит переход
         * @param to    - Цвет, к которому происходит переход
         * @param ratio - Текущее положение трансформации
         * @return - Промежуточный цвет
         */
        public static int blend(int from, int to, @FloatRange(from = 0.0, to = 1.0) float ratio) {
            final float inverseRatio = 1f - ratio;

            final float r = Color.red(to) * ratio + Color.red(from) * inverseRatio;
            final float g = Color.green(to) * ratio + Color.green(from) * inverseRatio;
            final float b = Color.blue(to) * ratio + Color.blue(from) * inverseRatio;

            return Color.rgb((int) r, (int) g, (int) b);
        }

    }

    public static final class Draw {

        /**
         * Получение покрашенного изображения
         *
         * @param drawableId - Идентификатор изображения
         * @param attr       - Аттрибут цвета
         * @return - Покрашенное изображение
         */
        public static Drawable get(Context context, @DrawableRes int drawableId, @AttrRes int attr) {
            final Drawable drawable = ContextCompat.getDrawable(context, drawableId);

            final int color = Clr.get(context, attr);
            if (drawable != null) {
                drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            }

            return drawable;
        }

    }

    public static final class Tint {

        /**
         * Покараска элемента меню в стандартный цвет
         *
         * @param item - Элемент меню
         */
        public static void menuIcon(Context context, MenuItem item) {
            final Drawable drawable = item.getIcon();
            final Drawable wrapDrawable = DrawableCompat.wrap(drawable);

            final int color = Clr.get(context, R.attr.clIcon);
            DrawableCompat.setTint(wrapDrawable, color);

            item.setIcon(wrapDrawable);
        }

        /**
         * Блокирование кнопки и установка на неё покрашенной иконки в зависимости
         * от текстового сообщения
         *
         * @param button     - Кнопка, которую необходимо контролировать
         * @param drawableId - Идентификатор изображения
         * @param clEnable   - Цвет изображения, когда кнопка доступна для нажатия
         * @param text       - Текстовое сообщение, от которого зависит иконка и блокировка
         */
        public static void button(Context context, ImageButton button, @DrawableRes int drawableId,
                                  @AttrRes int clEnable, String text) {
            final Drawable drawableEnable = Draw.get(context, drawableId, clEnable);
            final Drawable drawableDisable = Draw.get(context, drawableId, R.attr.clSecond);

            if (!TextUtils.isEmpty(text)) {
                button.setImageDrawable(drawableEnable);
                button.setEnabled(true);
            } else {
                button.setImageDrawable(drawableDisable);
                button.setEnabled(false);
            }
        }

        /**
         * Блокирование кнопки и установка на неё покрашенной иконки в зависимости
         * от текстового сообщения и дополнительного параметра доступа
         *
         * @param button     - Кнопка, которую необходимо контролировать
         * @param drawableId - Идентификатор изображения
         * @param text       - Текстовое сообщение, от которого зависит иконка и блокировка
         * @param enable     - Дополнительный параметр для контроля, например список содержит
         *                   текстовое сообщение
         */
        public static void button(Context context, ImageButton button, @DrawableRes int drawableId,
                                  String text, boolean enable) {
            final Drawable drawableEnable = Draw.get(context, drawableId, R.attr.clAccent);
            final Drawable drawableDisable = Draw.get(context, drawableId, R.attr.clSecond);

            if (!TextUtils.isEmpty(text)) {
                button.setImageDrawable(enable
                        ? drawableEnable
                        : drawableDisable);
                button.setEnabled(enable);
            } else {
                button.setImageDrawable(drawableDisable);
                button.setEnabled(false);
            }
        }
    }

    public static final class Note {

        /**
         * @param listRoll - Список для проверки
         * @return - Количество отмеченных пунктов
         */
        public static int getRollCheck(List<RollItem> listRoll) {
            int rollCheck = 0;
            for (RollItem rollItem : listRoll) {
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
        public static boolean isAllCheck(List<RollItem> listRoll) {
            if (listRoll.size() != 0) {
                for (RollItem rollItem : listRoll) {
                    if (!rollItem.isCheck()) {
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }
        }

    }

    public static final class Pref {

        /**
         * @param context - Для получения настроек
         * @return - Формирование поискового запроса относительно настроек
         */
        public static String getSortNoteOrder(Context context) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

            String keysStr = pref.getString(context.getString(R.string.pref_key_sort), SortDef.def);
            String[] keysArr = keysStr.split(SortDef.divider);

            StringBuilder order = new StringBuilder();
            for (String aKey : keysArr) {
                @SortDef int key = Integer.parseInt(aKey);

                order.append(DbAnn.orders[key]);

                if (key != SortDef.create && key != SortDef.change) {
                    order.append(SortDef.divider);
                } else break;
            }

            return order.toString();
        }

        /**
         * @param listSort - Список моделей из диалога
         * @return - Строка сортировки
         */
        public static String getSortByList(List<SortItem> listSort) {
            StringBuilder order = new StringBuilder();
            for (int i = 0; i < listSort.size(); i++) {
                order.append(Integer.toString(listSort.get(i).getKey()));

                if (i != listSort.size() - 1) {
                    order.append(SortDef.divider);
                }
            }
            return order.toString();
        }

        public static String getSortSummary(Context context, String keys) {
            StringBuilder order = new StringBuilder();

            String[] keysArr = keys.split(SortDef.divider);
            String[] keysName = context.getResources().getStringArray(R.array.pref_sort_text);

            for (int k = 0; k < keysArr.length; k++) {
                @SortDef int key = Integer.parseInt(keysArr[k]);

                String summary = keysName[key];
                if (k != 0) {
                    summary = summary.replace(context.getString(R.string.pref_sort_summary_start), "").replaceFirst(" ", "");
                }

                order.append(summary);
                if (key != SortDef.create && key != SortDef.change) {
                    order.append(SortDef.divider);
                } else break;

            }

            return order.toString();
        }

        public static boolean getSortEqual(String keys1, String keys2) {
            String[] keysArr1 = keys1.split(SortDef.divider);
            String[] keysArr2 = keys2.split(SortDef.divider);

            for (int i = 0; i < keysArr1.length; i++) {
                if (!keysArr1[i].equals(keysArr2[i])) {
                    return false;
                }
                if (keysArr1[i].equals(Integer.toString(SortDef.create)) || keysArr1[i].equals(Integer.toString(SortDef.change))) {
                    break;
                }
            }

            return true;
        }

        public static void listAllPref(Context context, TextView textView) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

            textView.append("\n\nSort:");
            textView.append("\nNt: " + pref.getString(context.getString(R.string.pref_key_sort), SortDef.def));

            textView.append("\n\nNotes:");
            textView.append("\nColorDef: " + pref.getInt(context.getString(R.string.pref_key_color), context.getResources().getInteger(R.integer.pref_color_default)));
            textView.append("\nPause:\t" + pref.getBoolean(context.getString(R.string.pref_key_pause_save), context.getResources().getBoolean(R.bool.pref_pause_save_default)));
            textView.append("\nSave:\t" + pref.getBoolean(context.getString(R.string.pref_key_auto_save), context.getResources().getBoolean(R.bool.pref_auto_save_default)));
            textView.append("\nSTime:\t" + pref.getInt(context.getString(R.string.pref_key_save_time), context.getResources().getInteger(R.integer.pref_save_time_default)));
            textView.append("\nTheme:\t" + pref.getInt(context.getString(R.string.pref_key_theme), context.getResources().getInteger(R.integer.pref_theme_default)));
        }

        public static int getTheme(Context context) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            return pref.getInt(context.getString(R.string.pref_key_theme), context.getResources().getInteger(R.integer.pref_theme_default));
        }

    }

    public static final class Time {

        /**
         * @return - Текущее время в нужном формате
         */
        public static String getCurrentTime(Context context) {
            return new SimpleDateFormat(context.getString(R.string.date_app_format), Locale.getDefault()).format(Calendar.getInstance().getTime());
        }

        /**
         * @param date - Время создания/изменения заметки
         * @return - Время и дата в приятном виде
         */
        public static String formatNoteDate(Context context, String date) {
            final DateFormat oldFormat = new SimpleDateFormat(context.getString(R.string.date_app_format), Locale.getDefault());
            final Calendar calendar = Calendar.getInstance();

            DateFormat newFormat;
            try {
                calendar.setTime(oldFormat.parse(date));
                if (DateUtils.isToday(calendar.getTimeInMillis()))
                    newFormat = new SimpleDateFormat(context.getString(R.string.date_note_today_format), Locale.getDefault());
                else
                    newFormat = new SimpleDateFormat(context.getString(R.string.date_note_yesterday_format), Locale.getDefault());
                return newFormat.format(oldFormat.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }

    }

}
