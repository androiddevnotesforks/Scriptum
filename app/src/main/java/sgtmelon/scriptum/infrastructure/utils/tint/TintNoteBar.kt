package sgtmelon.scriptum.infrastructure.utils.tint

import android.content.Context
import androidx.annotation.ColorInt
import sgtmelon.scriptum.cleanup.extension.getAppSimpleColor
import sgtmelon.scriptum.cleanup.extension.getNoteToolbarColor
import sgtmelon.scriptum.infrastructure.model.key.ColorShade
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.model.key.preference.Color

/**
 * Parent class for tinting different application bar's (e.g. toolbar).
 */
abstract class TintNoteBar(protected val context: Context) {

    protected data class Colors(@ColorInt val bar: Int, @ColorInt val indicator: Int)

    protected fun getColors(theme: ThemeDisplayed, color: Color): Colors {
        return Colors(
            context.getNoteToolbarColor(theme, color, needDark = false),
            context.getAppSimpleColor(color, ColorShade.DARK)
        )
    }
}