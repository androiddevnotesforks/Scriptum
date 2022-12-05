package sgtmelon.scriptum.cleanup.extension

import android.content.Context
import android.content.res.Configuration
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.utils.extensions.recordException

fun Context.getDisplayedTheme(): ThemeDisplayed? {
    val uiMode = resources.configuration.uiMode

    return when (uiMode and Configuration.UI_MODE_NIGHT_MASK) {
        Configuration.UI_MODE_NIGHT_NO -> ThemeDisplayed.LIGHT
        Configuration.UI_MODE_NIGHT_YES -> ThemeDisplayed.DARK
        else -> run {
            recordException("Unknown configuration! Received null for uiMode=$uiMode")
            return@run null
        }
    }
}