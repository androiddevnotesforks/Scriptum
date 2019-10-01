package sgtmelon.scriptum.extension

import android.view.View
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import sgtmelon.extension.*
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.data.ColorData
import sgtmelon.scriptum.model.item.ColorItem
import java.text.SimpleDateFormat
import java.util.*

//region Color and Theme

/**
 * Установка цвета карточки в соответствии с цветом заметки
 */
@BindingAdapter("noteColor")
fun CardView.bindNoteColor(@Color color: Int) =
        setCardBackgroundColor(context.getAppThemeColor(color, needDark = false))

/**
 * Установка цвета для индикатора на основании темы
 */
@BindingAdapter(value = ["indicatorTheme", "indicatorColor"])
fun View.bindIndicatorColor(@Theme theme: Int, @Color color: Int): ColorItem {
    val colorItem = ColorData.getColorItem(theme, color)

    background.setColor(context, colorItem)

    return colorItem
}

/**
 * Установка видимости элемента только на конкретной теме
 */
@BindingAdapter(value = ["visibleTheme", "visibleOn"])
fun View.bindVisibleTheme(@Theme visibleOn: Int, @Theme currentTheme: Int) {
    visibility = if (visibleOn == currentTheme) View.VISIBLE else View.GONE
}

@BindingAdapter(value = ["drawableId", "colorAttr"])
fun ImageView.bindDrawable(@DrawableRes drawableId: Int, @AttrRes color: Int) {
    setImageDrawable(ContextCompat.getDrawable(context, drawableId))
    setColorFilter(context.getColorAttr(color))
}

//endregion

//region Boolean bind

/**
 * Установка цветового фильтра на основании результата логического выражения
 *
 * @param boolExpression - Логическое выражение
 * @param trueColor      - Цвет при истине
 * @param falseColor     - Цвет если ложь
 */
@BindingAdapter(value = ["boolExpression", "trueColor", "falseColor"])
fun ImageButton.bindBoolTint(boolExpression: Boolean,
                             @AttrRes trueColor: Int,
                             @AttrRes falseColor: Int) =
        setColorFilter(context.getColorAttr(if (boolExpression) trueColor else falseColor))

@BindingAdapter(value = ["boolExpression", "trueColor", "falseColor"])
fun TextView.bindBoolTextColor(boolExpression: Boolean,
                               @AttrRes trueColor: Int,
                               @AttrRes falseColor: Int) =
        setTextColor(context.getColorAttr(if (boolExpression) trueColor else falseColor))

/**
 * Установка доступа к [ImageButton]
 */
@BindingAdapter("enabled")
fun ImageButton.bindEnabled(enabled: Boolean) {
    isEnabled = enabled
}

/**
 * Изменение состояния [CheckBox] с анимацией или установка значения [checkState]
 */
@BindingAdapter(value = ["checkToggle", "checkState"])
fun CheckBox.bindCheck(checkToggle: Boolean, checkState: Boolean) =
        if (checkToggle) toggle() else isChecked = checkState

//endregion

//region Time

/**
 * Форматировение строки с прошедшим временем в подобающий вид
 */
@BindingAdapter(value = ["pastTime"])
fun TextView.bindPastTime(dateTime: String) {
    text = try {
        val locale = Locale.getDefault()
        val calendar = Calendar.getInstance().apply {
            time = getDateFormat().parse(dateTime)
        }

        SimpleDateFormat(when {
            calendar.isToday() -> if (context.is24Format()) context.getString(R.string.format_time) else context.getString(R.string.format_time_am)
            calendar.isThisYear() -> context.getString(R.string.format_date_medium)
            else -> context.getString(R.string.format_date_short)
        }, locale).format(calendar.time)
    } catch (e: Throwable) {
        e.printStackTrace()
        null
    }
}

/**
 * Форматировение строки с будущим временем в подобающий вид
 */
@BindingAdapter(value = ["futureTime"])
fun TextView.bindFutureTime(dateTime: String) {
    text = try {
        dateTime.getCalendar().formatFuture(context)
    } catch (e: Throwable) {
        e.printStackTrace()
        null
    }
}

//endregion