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

    @Test fun colorRed() = startTest(Theme.LIGHT, Color.red)

    @Test fun colorPurple() = startTest(Theme.LIGHT, Color.purple)

    @Test fun colorIndigo() = startTest(Theme.LIGHT, Color.indigo)

    @Test fun colorBlue() = startTest(Theme.LIGHT, Color.blue)

    @Test fun colorTeal() = startTest(Theme.LIGHT, Color.teal)

    @Test fun colorGreen() = startTest(Theme.LIGHT, Color.green)

    @Test fun colorYellow() = startTest(Theme.LIGHT, Color.yellow)

    @Test fun colorOrange() = startTest(Theme.LIGHT, Color.orange)

    @Test fun colorBrown() = startTest(Theme.LIGHT, Color.brown)

    @Test fun colorBlueGrey() = startTest(Theme.LIGHT, Color.blueGrey)

    @Test fun colorWhite() = startTest(Theme.LIGHT, Color.white)

}