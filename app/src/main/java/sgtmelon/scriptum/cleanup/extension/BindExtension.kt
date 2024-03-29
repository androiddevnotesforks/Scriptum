package sgtmelon.scriptum.cleanup.extension

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import sgtmelon.extensions.formatFuture
import sgtmelon.extensions.formatPast
import sgtmelon.extensions.getColorAttr
import sgtmelon.extensions.getDrawableCompat
import sgtmelon.extensions.toCalendar
import sgtmelon.scriptum.infrastructure.model.data.ColorData
import sgtmelon.scriptum.infrastructure.model.item.ColorItem
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.utils.extensions.setColor

//region Color and Theme

@BindingAdapter(value = ["noteColor"])
fun CardView.bindNoteColor(color: Color) {
    setCardBackgroundColor(context.getNoteCardColor(color))
}

@BindingAdapter(value = ["indicatorColor"])
fun View.bindIndicatorColor(color: Color): ColorItem? {
    val theme = context.getDisplayedTheme() ?: return null
    val colorItem = ColorData.getColorItem(theme, color)

    background.setColor(context, colorItem)

    return colorItem
}

@BindingAdapter(value = ["drawableId", "colorAttr"])
fun ImageView.bindDrawable(@DrawableRes drawableId: Int, @AttrRes color: Int) {
    bindDrawable(drawableId)
    setColorFilter(context.getColorAttr(color))
}

fun ImageView.bindDrawable(@DrawableRes drawableId: Int) {
    if (drawableId == 0) {
        visibility = View.GONE
        return
    }

    setImageDrawable(context.getDrawableCompat(drawableId))
}

//endregion

//region Boolean bind

/**
 * Set tint rely on [boolExpression]
 */
@BindingAdapter(value = ["boolExpression", "trueColor", "falseColor"])
fun ImageButton.bindBoolTint(boolExpression: Boolean,
                             @AttrRes trueColor: Int,
                             @AttrRes falseColor: Int) {
    setColorFilter(context.getColorAttr(if (boolExpression) trueColor else falseColor))
}

/**
 * Set textColor rely on [boolExpression]
 */
@BindingAdapter(value = ["boolExpression", "trueColor", "falseColor"])
fun TextView.bindTextColor(boolExpression: Boolean,
                             @AttrRes trueColor: Int,
                             @AttrRes falseColor: Int) {
    setTextColor(context.getColorAttr(if (boolExpression) trueColor else falseColor))
}

//endregion

//region Text

@BindingAdapter(value = ["stringId"])
fun TextView.bindText(@StringRes stringId: Int) {
    if (stringId == 0) return
    setText(stringId)
}

@BindingAdapter(value = ["pastTime"])
fun TextView.bindPastTime(dateTime: String) {
    text = try {
        dateTime.toCalendar().formatPast()
    } catch (e: Throwable) {
        e.printStackTrace()
        null
    }
}

@BindingAdapter(value = ["futureTime"])
fun TextView.bindFutureTime(dateTime: String) {
    text = try {
        dateTime.toCalendar().formatFuture(context)
    } catch (e: Throwable) {
        e.printStackTrace()
        null
    }
}

//endregion