package sgtmelon.scriptum.test.control.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.ui.notification.AlarmActivity

/**
 * Тест работы анимации и цветовой схемы экрана [AlarmActivity] в светлой теме
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class AlarmAnimLightTest : AlarmAnimParentTest() {

    @Test fun colorRed() = startTest(Theme.LIGHT, Color.RED)

    @Test fun colorPurple() = startTest(Theme.LIGHT, Color.PURPLE)

    @Test fun colorIndigo() = startTest(Theme.LIGHT, Color.INDIGO)

    @Test fun colorBlue() = startTest(Theme.LIGHT, Color.BLUE)

    @Test fun colorTeal() = startTest(Theme.LIGHT, Color.TEAL)

    @Test fun colorGreen() = startTest(Theme.LIGHT, Color.GREEN)

    @Test fun colorYellow() = startTest(Theme.LIGHT, Color.YELLOW)

    @Test fun colorOrange() = startTest(Theme.LIGHT, Color.ORANGE)

    @Test fun colorBrown() = startTest(Theme.LIGHT, Color.BROWN)

    @Test fun colorBlueGrey() = startTest(Theme.LIGHT, Color.BLUE_GREY)

    @Test fun colorWhite() = startTest(Theme.LIGHT, Color.WHITE)

}