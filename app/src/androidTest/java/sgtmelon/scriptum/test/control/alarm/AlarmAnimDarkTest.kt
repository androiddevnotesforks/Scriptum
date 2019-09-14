package sgtmelon.scriptum.test.control.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.ui.notification.AlarmActivity

/**
 * Test of animation and color scheme of [AlarmActivity] in dark theme
 */
@RunWith(AndroidJUnit4::class)
class AlarmAnimDarkTest: AlarmAnimParentTest() {

    @Test fun colorRed() = startTest(Theme.DARK, Color.RED)

    @Test fun colorPurple() = startTest(Theme.DARK, Color.PURPLE)

    @Test fun colorIndigo() = startTest(Theme.DARK, Color.INDIGO)

    @Test fun colorBlue() = startTest(Theme.DARK, Color.BLUE)

    @Test fun colorTeal() = startTest(Theme.DARK, Color.TEAL)

    @Test fun colorGreen() = startTest(Theme.DARK, Color.GREEN)

    @Test fun colorYellow() = startTest(Theme.DARK, Color.YELLOW)

    @Test fun colorOrange() = startTest(Theme.DARK, Color.ORANGE)

    @Test fun colorBrown() = startTest(Theme.DARK, Color.BROWN)

    @Test fun colorBlueGrey() = startTest(Theme.DARK, Color.BLUE_GREY)

    @Test fun colorWhite() = startTest(Theme.DARK, Color.WHITE)

}