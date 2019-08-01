package sgtmelon.scriptum.test.control.alarm

import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.ui.SplashActivity
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
            this.theme = theme

            signal = Random.nextInt(from = 1, until = 7)
            melodyUri = melodyList.random().uri
            volume = Random.nextInt(from = 50, until = 100)
            volumeIncrease = Random.nextBoolean()
        }

        val noteModel = testData.clear().let {
            if (Random.nextBoolean()) {
                it.insertText(it.textNote.apply { this.color = color })
            } else {
                it.insertRoll(it.rollNote.apply { this.color = color })
            }
        }

        launch(intent = SplashActivity.getAlarmInstance(context, noteModel.noteEntity)) {
            waitAfter(time = 15000) { openAlarm(noteModel) }
        }
    }

}