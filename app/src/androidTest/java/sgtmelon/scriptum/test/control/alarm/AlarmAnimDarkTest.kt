package sgtmelon.scriptum.test.control.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.view.notification.AlarmActivity

/**
 * Тест работы анимации и цветовой схемы экрана [AlarmActivity] в тёмной теме
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class AlarmAnimDarkTest: AlarmAnimParentTest() {

    @Test fun colorRed() = startTest(Theme.dark, Color.red)

    @Test fun colorPurple() = startTest(Theme.dark, Color.purple)

    @Test fun colorIndigo() = startTest(Theme.dark, Color.indigo)

    @Test fun colorBlue() = startTest(Theme.dark, Color.blue)

    @Test fun colorTeal() = startTest(Theme.dark, Color.teal)

    @Test fun colorGreen() = startTest(Theme.dark, Color.green)

    @Test fun colorYellow() = startTest(Theme.dark, Color.yellow)

    @Test fun colorOrange() = startTest(Theme.dark, Color.orange)

    @Test fun colorBrown() = startTest(Theme.dark, Color.brown)

    @Test fun colorBlueGrey() = startTest(Theme.dark, Color.blueGrey)

    @Test fun colorWhite() = startTest(Theme.dark, Color.white)

}