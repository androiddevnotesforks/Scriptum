package sgtmelon.scriptum.infrastructure.widgets.ripple

import android.graphics.Paint
import sgtmelon.scriptum.infrastructure.model.key.ColorShade
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed

class RippleConverter {

    fun getRippleShade(theme: ThemeDisplayed): ColorShade {
        return when (theme) {
            ThemeDisplayed.LIGHT -> ColorShade.ACCENT
            ThemeDisplayed.DARK -> ColorShade.DARK
        }
    }

    fun getPaintStyle(theme: ThemeDisplayed): Paint.Style {
        return when (theme) {
            ThemeDisplayed.LIGHT -> Paint.Style.STROKE
            ThemeDisplayed.DARK -> Paint.Style.FILL
        }
    }
}