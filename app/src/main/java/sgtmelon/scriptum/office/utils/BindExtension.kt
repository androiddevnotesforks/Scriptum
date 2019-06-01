package sgtmelon.scriptum.office.utils

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
 * Установка цвета для индикатора
 */
@BindingAdapter("indicatorColor")
fun View.bindIndicatorColor(@ColorDef color: Int) =
        tintColorIndicator(ColorData.getColorItem(Preference(context).theme, color))

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
 * Установка видимости элемента только на конкретной теме
 */
@BindingAdapter("visibleOn")
fun View.bindVisibleOn(@ThemeDef visibleOn: Int) {
    visibility = if (Preference(context).theme == visibleOn) View.VISIBLE else View.GONE
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