package sgtmelon.scriptum.model.annotation

import androidx.annotation.IntDef


/**
 * Describes standard themes
 */
@IntDef(Theme.UNDEFINED, Theme.LIGHT, Theme.DARK)
annotation class Theme {

    companion object {
        const val UNDEFINED = -1
        const val LIGHT = 0
        const val DARK = 1
    }

}