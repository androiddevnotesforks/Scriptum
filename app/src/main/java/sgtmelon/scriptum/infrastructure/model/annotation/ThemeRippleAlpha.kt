package sgtmelon.scriptum.infrastructure.model.annotation

import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed

/**
 * Class to declare alpha ration (from 0 to 255) depend on [ThemeDisplayed].
 */
annotation class ThemeRippleAlpha {
    companion object {
        const val LIGHT = 128
        const val DARK = 64
    }
}
