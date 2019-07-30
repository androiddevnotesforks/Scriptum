package sgtmelon.scriptum.model.annotation

import androidx.annotation.IntDef


/**
 * Для описания стандартных тем
 *
 * @author SerjantArbuz
 */
@IntDef(Theme.UNDEFINED, Theme.light, Theme.dark)
annotation class Theme {

    companion object {
        const val UNDEFINED = -1
        const val light = 0
        const val dark = 1
    }

}