package sgtmelon.scriptum.test.ui.control.anim.alarm

import sgtmelon.scriptum.basic.extension.waitAfter
import sgtmelon.scriptum.cleanup.domain.model.annotation.Color
import sgtmelon.scriptum.cleanup.domain.model.annotation.Theme
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.notification.AlarmActivity
import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.test.parent.situation.IColorTest
import kotlin.random.Random

/**
 * Parent class for tests of [AlarmActivity] with different themes.
 */
abstract class ParentAlarmAnimTest(@Theme private val theme: Int) : ParentUiTest(), IColorTest {

    override fun startTest(@Color value: Int) {
        setupTheme(theme)

        with(appPreferences) {
            signal = Random.nextInt(from = 1, until = 3)
            volume = Random.nextInt(from = 50, until = 100)
            volumeIncrease = Random.nextBoolean()
        }

        val noteItem = data.clear().let {
            return@let if (Random.nextBoolean()) {
                it.insertText(it.textNote.apply { this.color = value })
            } else {
                it.insertRoll(it.rollNote.apply { this.color = value })
            }
        }

        launchAlarm(noteItem) { waitAfter(TEST_TIME) { openAlarm(noteItem) } }
    }

    companion object {
        private const val TEST_TIME = 6000L
    }
}