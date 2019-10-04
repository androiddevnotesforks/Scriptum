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
class AlarmAnimDarkTest: AlarmAnimParentTest(Theme.DARK) {

    @Test override fun colorRed() = startTest(Color.RED)

    @Test override fun colorPurple() = startTest(Color.PURPLE)

    @Test override fun colorIndigo() = startTest(Color.INDIGO)

    @Test override fun colorBlue() = startTest(Color.BLUE)

    @Test override fun colorTeal() = startTest(Color.TEAL)

    @Test override fun colorGreen() = startTest(Color.GREEN)

    @Test override fun colorYellow() = startTest(Color.YELLOW)

    @Test override fun colorOrange() = startTest(Color.ORANGE)

    @Test override fun colorBrown() = startTest(Color.BROWN)

    @Test override fun colorBlueGrey() = startTest(Color.BLUE_GREY)

    @Test override fun colorWhite() = startTest(Color.WHITE)

}