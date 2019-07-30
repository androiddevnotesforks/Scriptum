package sgtmelon.scriptum.test.control.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.ui.notification.AlarmActivity

/**
 * Тест работы анимации и цветовой схемы экрана [AlarmActivity] в тёмной теме
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class AlarmAnimDarkTest: AlarmAnimParentTest() {

    @Test fun colorRed() = startTest(Theme.DARK, Color.red)

    @Test fun colorPurple() = startTest(Theme.DARK, Color.purple)

    @Test fun colorIndigo() = startTest(Theme.DARK, Color.indigo)

    @Test fun colorBlue() = startTest(Theme.DARK, Color.blue)

    @Test fun colorTeal() = startTest(Theme.DARK, Color.teal)

    @Test fun colorGreen() = startTest(Theme.DARK, Color.green)

    @Test fun colorYellow() = startTest(Theme.DARK, Color.yellow)

    @Test fun colorOrange() = startTest(Theme.DARK, Color.orange)

    @Test fun colorBrown() = startTest(Theme.DARK, Color.brown)

    @Test fun colorBlueGrey() = startTest(Theme.DARK, Color.blueGrey)

    @Test fun colorWhite() = startTest(Theme.DARK, Color.white)

}