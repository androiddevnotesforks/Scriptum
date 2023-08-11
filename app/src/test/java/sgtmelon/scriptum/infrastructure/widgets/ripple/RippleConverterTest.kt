package sgtmelon.scriptum.infrastructure.widgets.ripple

import android.graphics.Paint
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.infrastructure.model.key.ColorShade
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed

/**
 * Test for [RippleConverter].
 */
class RippleConverterTest : sgtmelon.tests.uniter.ParentTest() {

    private val converter = RippleConverter()

    @Test fun getRippleShade() {
        for (theme in ThemeDisplayed.values()) {
            assertEquals(
                converter.getRippleShade(theme), when (theme) {
                    ThemeDisplayed.LIGHT -> ColorShade.ACCENT
                    ThemeDisplayed.DARK -> ColorShade.DARK
                }
            )
        }
    }

    @Test fun getPaintStyle() {
        for (theme in ThemeDisplayed.values()) {
            assertEquals(
                converter.getPaintStyle(theme), when (theme) {
                    ThemeDisplayed.LIGHT -> Paint.Style.STROKE
                    ThemeDisplayed.DARK -> Paint.Style.FILL
                }
            )
        }
    }
}