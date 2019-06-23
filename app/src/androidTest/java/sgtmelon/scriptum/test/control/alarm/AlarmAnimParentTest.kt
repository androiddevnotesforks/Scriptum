package sgtmelon.scriptum.test.control.alarm

import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.view.SplashActivity.Companion.getSplashAlarmIntent
import sgtmelon.scriptum.test.ParentUiTest
import sgtmelon.scriptum.waitAfter
import kotlin.random.Random

abstract class AlarmAnimParentTest : ParentUiTest() {

    override fun setUp() {
        super.setUp()
        iPreferenceRepo.firstStart = false
    }

    protected fun startTest(@Theme theme: Int, @Color color: Int) {
        iPreferenceRepo.theme = theme

        val noteModel = testData.clear().let {
            if (Random.nextBoolean()) {
                it.insertText(it.textNote.apply { this.color = color })
            } else {
                it.insertRoll(it.rollNote.apply { this.color = color })
            }
        }

        launch(intent = context.getSplashAlarmIntent(noteModel.noteEntity)) {
            waitAfter(time = 15000) { openAlarm(noteModel) }
        }
    }

}