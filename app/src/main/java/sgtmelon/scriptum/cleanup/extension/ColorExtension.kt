package sgtmelon.scriptum.cleanup.extension

import android.content.Context
import androidx.annotation.ColorInt
import sgtmelon.extensions.getColorAttr
import sgtmelon.extensions.getColorCompat
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.model.data.ColorData.accent
import sgtmelon.scriptum.infrastructure.model.data.ColorData.dark
import sgtmelon.scriptum.infrastructure.model.data.ColorData.light
import sgtmelon.scriptum.infrastructure.model.key.ColorShade
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.model.key.preference.Color

/**
 * Get note color for toolbars rely on theme and background
 * [needDark] - If element place on dark background (e.g. note color indicator)
 */
@ColorInt
fun Context.getNoteToolbarColor(theme: ThemeDisplayed, color: Color, needDark: Boolean): Int {
    val ordinal = color.ordinal

    return if (theme == ThemeDisplayed.LIGHT) {
        if (needDark) getColorCompat(dark[ordinal])
        else getColorCompat(light[ordinal])
    } else {
        if (needDark) getColorCompat(dark[ordinal])
        else getColorAttr(R.attr.clPrimary)
    }
}

/**
 * Get note color for cards
 */
@ColorInt
fun Context.getNoteCardColor(color: Color): Int = getColorCompat(light[color.ordinal])

@ColorInt
fun Context.getAppSimpleColor(color: Color, shade: ColorShade): Int {
    val ordinal = color.ordinal

    return getColorCompat(
        when (shade) {
            ColorShade.LIGHT -> light[ordinal]
            ColorShade.ACCENT -> accent[ordinal]
            ColorShade.DARK -> dark[ordinal]
        }
    )
}