package sgtmelon.scriptum.model.annotation

import androidx.annotation.IntDef


/**
 * Для описания стандартных тем
 *
 * @author SerjantArbuz
 */
@IntDef(Theme.UNDEFINED, Theme.LIGHT, Theme.DARK)
annotation class Theme {

    companion object {
        const val UNDEFINED = -1
        const val LIGHT = 0
        const val DARK = 1
    }

}