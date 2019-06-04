package sgtmelon.scriptum.test.control

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.view.SplashActivity.Companion.getSplashAlarmIntent
import sgtmelon.scriptum.screen.view.notification.AlarmActivity
import sgtmelon.scriptum.test.ParentTest
import kotlin.random.Random

/**
 * Тест работы анимации и цветовой схемы экрана [AlarmActivity]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class AlarmAnimTest : ParentTest() {

    override fun setUp() {
        super.setUp()
        preference.setFirstStart(false)
    }

    private fun startTest(@Theme theme: Int, @Color color: Int) {
        preference.setTheme(theme)

        val noteItem = testData.clear().let {
            if (Random.nextBoolean()) {
                it.insertText(it.textNote.apply { this.color = color })
            } else {
                it.insertRoll(it.rollNote.apply { this.color = color })
            }
        }

        launch(intent = context.getSplashAlarmIntent(noteItem)) {
            openAlarm(noteItem) { wait(time = 10000) }
        }
    }

    //region Theme light

    @Test fun lightColor0() = startTest(Theme.light, Color.red)

    @Test fun lightColor1() = startTest(Theme.light, Color.purple)

    @Test fun lightColor2() = startTest(Theme.light, Color.indigo)

    @Test fun lightColor3() = startTest(Theme.light, Color.blue)

    @Test fun lightColor4() = startTest(Theme.light, Color.teal)

    @Test fun lightColor5() = startTest(Theme.light, Color.green)

    @Test fun lightColor6() = startTest(Theme.light, Color.yellow)

    @Test fun lightColor7() = startTest(Theme.light, Color.orange)

    @Test fun lightColor8() = startTest(Theme.light, Color.brown)

    @Test fun lightColor9() = startTest(Theme.light, Color.blueGrey)

    @Test fun lightColor10() = startTest(Theme.light, Color.white)

    //endregion

    //region Theme dark

    @Test fun darkColor0() = startTest(Theme.dark, Color.red)

    @Test fun darkColor1() = startTest(Theme.dark, Color.purple)

    @Test fun darkColor2() = startTest(Theme.dark, Color.indigo)

    @Test fun darkColor3() = startTest(Theme.dark, Color.blue)

    @Test fun darkColor4() = startTest(Theme.dark, Color.teal)

    @Test fun darkColor5() = startTest(Theme.dark, Color.green)

    @Test fun darkColor6() = startTest(Theme.dark, Color.yellow)

    @Test fun darkColor7() = startTest(Theme.dark, Color.orange)

    @Test fun darkColor8() = startTest(Theme.dark, Color.brown)

    @Test fun darkColor9() = startTest(Theme.dark, Color.blueGrey)

    @Test fun darkColor10() = startTest(Theme.dark, Color.white)

    //endregion

}