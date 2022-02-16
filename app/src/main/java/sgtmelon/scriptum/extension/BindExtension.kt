package sgtmelon.scriptum.extension

import android.view.View
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import sgtmelon.common.formatFuture
import sgtmelon.common.formatPast
import sgtmelon.common.getCalendar
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.data.ColorData
import sgtmelon.scriptum.domain.model.item.ColorItem

//region Color and Theme

@BindingAdapter(value = ["noteColor"])
fun CardView.bindNoteColor(@Color color: Int) {
    setCardBackgroundColor(context.getNoteCardColor(color))
}

@BindingAdapter(value = ["indicatorColor"])
fun View.bindIndicatorColor(@Color color: Int): ColorItem? {
    val theme = context.getAppTheme() ?: return null
    val colorItem = ColorData.getColorItem(theme, color)

    background.setColor(context, colorItem)

    return colorItem
}

/**
 * Set visibility rely on current theme
 */
@BindingAdapter(value = ["visibleTheme", "visibleOn"])
fun View.bindVisibleTheme(@Theme visibleOn: Int, @Theme theme: Int) {
    visibility = if (visibleOn == theme) View.VISIBLE else View.GONE
}

@BindingAdapter(value = ["drawableId", "colorAttr"])
fun ImageView.bindDrawable(@DrawableRes drawableId: Int, @AttrRes color: Int) {
    if (drawableId == 0) {
        visibility = View.GONE
        return
    }

    setImageDrawable(context.getDrawable(drawableId))
    setColorFilter(context.getColorAttr(color))
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



@BindingAdapter("enabled")
fun ImageButton.bindEnabled(enabled: Boolean) {
    isEnabled = enabled
}

/**
 * Change state of [CheckBox] with anim or simple value set
 */
@BindingAdapter(value = ["checkToggle", "checkState"])
fun CheckBox.bindCheck(checkToggle: Boolean, checkState: Boolean) {
    if (checkToggle) toggle() else isChecked = checkState
}

//endregion

//region Time

@BindingAdapter(value = ["pastTime"])
fun TextView.bindPastTime(dateTime: String) {
    text = try {
        dateTime.getCalendar().formatPast()
    } catch (e: Throwable) {
        e.printStackTrace()
        null
    }
}

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