package sgtmelon.scriptum.office.utils

import android.graphics.drawable.GradientDrawable
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
import sgtmelon.scriptum.model.data.ColorData
import sgtmelon.scriptum.model.item.ColorItem
import sgtmelon.scriptum.office.annot.def.ColorDef
import sgtmelon.scriptum.office.annot.def.ThemeDef
import sgtmelon.scriptum.office.utils.ColorUtils.getAppThemeColor
import sgtmelon.scriptum.office.utils.ColorUtils.getColorAttr

/**
 * Установка цвета карточки в соответствии с цветом заметки
 */
@BindingAdapter("noteColor")
fun CardView.bindNoteColor(@ColorDef color: Int) =
        setCardBackgroundColor(context.getAppThemeColor(color, needDark = false))

/**
 * Установка цвета для индикатора на основании темы
 */
@BindingAdapter(value = ["indicatorTheme", "indicatorColor"])
fun View.bindIndicatorColor(@ThemeDef theme: Int, @ColorDef color: Int): ColorItem {
    val colorItem = ColorData.getColorItem(theme, color)

    (background as? GradientDrawable)?.apply {
        setColor(context.getCompatColor(colorItem.fill))
        setStroke(context.getDimen(value = 1f), context.getCompatColor(colorItem.stroke))
    }

    return colorItem
}

/**
 * Установка видимости элемента только на конкретной теме
 */
@BindingAdapter(value = ["visibleTheme", "visibleOn"])
fun View.bindVisibleTheme(@ThemeDef visibleOn: Int, @ThemeDef currentTheme: Int) {
    visibility = if (visibleOn == currentTheme) View.VISIBLE else View.GONE
}

/**
 *
 */

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

@BindingAdapter(value = ["imageId", "imageColor"])
fun ImageView.bindImage(@DrawableRes drawableId: Int, @AttrRes color: Int) {
    setImageDrawable(ContextCompat.getDrawable(context, drawableId))
    setColorFilter(context.getColorAttr(color))
}

/**
 *
 */


/**
 * TODO сделать extension
 */
@BindingAdapter("pastTime")
fun TextView.bindPastTime(time: String) {
    text = TimeUtils.formatPast(context, time)
}

/**
 * TODO сделать extension
 */
@BindingAdapter("futureTime")
fun TextView.bindFutureTime(time: String) {
    text = TimeUtils.formatFuture(context, time)
}


/**
 *
 */


/**
 * Установка доступа к [ImageButton]
 */
@BindingAdapter("enabled")
fun ImageButton.bindEnabled(enabled: Boolean) {
    isEnabled = enabled
}


/**
 *
 */


/**
 * Изменение состояния [CheckBox] с анимацией или установка значения [checkState]
 */
@BindingAdapter(value = ["checkToggle", "checkState"])
fun CheckBox.bindCheck(checkToggle: Boolean, checkState: Boolean) =
        if (checkToggle) toggle() else isChecked = checkState