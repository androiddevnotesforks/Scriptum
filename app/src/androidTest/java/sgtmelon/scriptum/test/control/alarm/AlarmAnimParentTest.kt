package sgtmelon.scriptum.test.control.alarm

import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.ui.SplashActivity.Companion.getSplashAlarmIntent
import sgtmelon.scriptum.screen.ui.notification.AlarmActivity
import sgtmelon.scriptum.test.ParentUiTest
import sgtmelon.scriptum.waitAfter
import kotlin.random.Random

/**
 * Родительский класс для тестов [AlarmActivity] с разными темами
 *
 * @author SerjantArbuz
 */
abstract class AlarmAnimParentTest : ParentUiTest() {

    protected fun startTest(@Theme theme: Int, @Color color: Int) {
        with(iPreferenceRepo) {
            melodyUri = melodyList.random().uri
            this.theme = theme
        }

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