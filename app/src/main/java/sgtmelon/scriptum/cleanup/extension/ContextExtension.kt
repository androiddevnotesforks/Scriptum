package sgtmelon.scriptum.cleanup.extension

import android.content.Context
import android.content.res.Configuration
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.utils.record

fun Context.getDisplayedTheme(): ThemeDisplayed? {
    val uiMode = resources.configuration.uiMode

    return when (uiMode and Configuration.UI_MODE_NIGHT_MASK) {
        Configuration.UI_MODE_NIGHT_NO -> ThemeDisplayed.LIGHT
        Configuration.UI_MODE_NIGHT_YES -> ThemeDisplayed.DARK
        else -> run {
            NullPointerException("Unknown configuration! Received null for uiMode=$uiMode").record()
            return@run null
        }
    }
}

fun Context.isPortraitMode(): Boolean {
    return resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
}

inline fun <reified F : Fragment> FragmentManager.getFragmentByTag(tag: String): F? {
    return findFragmentByTag(tag) as? F
}