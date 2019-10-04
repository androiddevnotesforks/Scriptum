package sgtmelon.scriptum.test.control.alarm

import sgtmelon.scriptum.basic.extension.waitAfter
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.ui.notification.AlarmActivity
import sgtmelon.scriptum.test.ParentUiTest
import kotlin.random.Random

/**
 * Родительский класс для тестов [AlarmActivity] с разными темами
 */
abstract class AlarmAnimParentTest(@Theme private val theme: Int) : ParentUiTest() {

    abstract fun colorRed()
    abstract fun colorPurple()
    abstract fun colorIndigo()
    abstract fun colorBlue()
    abstract fun colorTeal()
    abstract fun colorGreen()
    abstract fun colorYellow()
    abstract fun colorOrange()
    abstract fun colorBrown()
    abstract fun colorBlueGrey()
    abstract fun colorWhite()

    protected fun startTest(@Color color: Int) {
        with(iPreferenceRepo) {
            theme = this@AlarmAnimParentTest.theme

            signal = Random.nextInt(from = 1, until = 3)
            volume = Random.nextInt(from = 50, until = 100)
            volumeIncrease = Random.nextBoolean()
        }

        val noteModel = data.clear().let {
            if (Random.nextBoolean()) {
                it.insertText(it.textNote.apply { this.color = color })
            } else {
                it.insertRoll(it.rollNote.apply { this.color = color })
            }
        }

        launchAlarm(noteModel) { waitAfter(TEST_TIME) { openAlarm(noteModel) } }
    }

    private companion object {
        const val TEST_TIME = 6000L
    }

}