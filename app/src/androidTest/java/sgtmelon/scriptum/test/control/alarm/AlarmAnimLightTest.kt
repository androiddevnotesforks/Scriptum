package sgtmelon.scriptum.test.control.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.view.notification.AlarmActivity

/**
 * Тест работы анимации и цветовой схемы экрана [AlarmActivity] в светлой теме
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class AlarmAnimLightTest : AlarmAnimParentTest() {

    @Test fun colorRed() = startTest(Theme.light, Color.red)

    @Test fun colorPurple() = startTest(Theme.light, Color.purple)

    @Test fun colorIndigo() = startTest(Theme.light, Color.indigo)

    @Test fun colorBlue() = startTest(Theme.light, Color.blue)

    @Test fun colorTeal() = startTest(Theme.light, Color.teal)

    @Test fun colorGreen() = startTest(Theme.light, Color.green)

    @Test fun colorYellow() = startTest(Theme.light, Color.yellow)

    @Test fun colorOrange() = startTest(Theme.light, Color.orange)

    @Test fun colorBrown() = startTest(Theme.light, Color.brown)

    @Test fun colorBlueGrey() = startTest(Theme.light, Color.blueGrey)

    @Test fun colorWhite() = startTest(Theme.light, Color.white)

}