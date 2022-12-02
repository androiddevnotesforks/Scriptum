package sgtmelon.scriptum.infrastructure.utils.tint

import android.content.Context
import sgtmelon.scriptum.cleanup.extension.getNoteToolbarColor
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.model.key.preference.Color

/**
 * Parent class for tinting different application bar's (e.g. toolbar).
 */
abstract class TintNoteBar(protected val context: Context) {

    protected fun getToolbarColor(theme: ThemeDisplayed, color: Color): Int {
        return context.getNoteToolbarColor(theme, color, needDark = false)
    }

    protected fun getStatusBarColor(theme: ThemeDisplayed, color: Color): Int {
        return context.getNoteToolbarColor(theme, color, needDark = false)
    }
}