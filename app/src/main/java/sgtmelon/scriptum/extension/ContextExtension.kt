package sgtmelon.scriptum.extension

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.data.ReceiverData
import sgtmelon.scriptum.presentation.screen.ui.ScriptumApplication

fun Context.getCompatColor(@ColorRes id: Int) = let { ContextCompat.getColor(it, id) }

@Theme
fun Context.getAppTheme(): Int? {
    val theme = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
        Configuration.UI_MODE_NIGHT_NO -> Theme.LIGHT
        Configuration.UI_MODE_NIGHT_YES -> Theme.DARK
        else -> null
    }

    ScriptumApplication.theme = theme

    return theme
}

//region Get resource value

@ColorInt
fun Context.getColorAttr(@AttrRes attr: Int): Int {
    val typedValue = TypedValue()

    theme.resolveAttribute(attr, typedValue, true)

    return getCompatColor(typedValue.resourceId)
}

@DimenRes
fun Context.getDimenAttr(@AttrRes attr: Int): Int {
    val typedValue = TypedValue()

    theme.resolveAttribute(attr, typedValue, true)

    return typedValue.resourceId
}

fun Context.getDimen(value: Float): Int {
    val unit = TypedValue.COMPLEX_UNIT_DIP
    val metrics = resources.displayMetrics

    return TypedValue.applyDimension(unit, value, metrics).toInt()
}

//endregion

fun Context.sendTo(place: String, command: String, extras: Intent.() -> Unit = {}) {
    sendBroadcast(Intent(place).apply {
        putExtra(ReceiverData.Values.COMMAND, command)
        putExtras(Intent().apply(extras))
    })
}

fun Context.isPortraitMode(): Boolean {
    return resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
}