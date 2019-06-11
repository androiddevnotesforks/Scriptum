package sgtmelon.scriptum.test.control.alarm

import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.view.SplashActivity.Companion.getSplashAlarmIntent
import sgtmelon.scriptum.test.ParentTest
import kotlin.random.Random

abstract class AlarmAnimParentTest : ParentTest() {

    override fun setUp() {
        super.setUp()
        preference.firstStart = false
    }

    protected fun startTest(@Theme theme: Int, @Color color: Int) {
        preference.theme = theme

        val noteItem = testData.clear().let {
            if (Random.nextBoolean()) {
                it.insertText(it.textNote.apply { this.color = color })
            } else {
                it.insertRoll(it.rollNote.apply { this.color = color })
            }
        }

        launch(intent = context.getSplashAlarmIntent(noteItem)) {
            openAlarm(noteItem) { wait(time = 15000) }
        }
    }

}