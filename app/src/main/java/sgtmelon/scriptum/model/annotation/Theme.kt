package sgtmelon.scriptum.model.annotation

import androidx.annotation.IntDef


/**
 * Аннотация для описания стандартных тем приложения
 *
 * @author SerjantArbuz
 */
@IntDef(Theme.light, Theme.dark)
annotation class Theme {

    companion object {
        const val light = 0
        const val dark = 1
    }

}