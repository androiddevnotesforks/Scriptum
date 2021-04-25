package sgtmelon.scriptum.test.control.anim.alarm

import sgtmelon.scriptum.basic.extension.waitAfter
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.presentation.screen.ui.impl.notification.AlarmActivity
import sgtmelon.scriptum.test.IColorTest
import sgtmelon.scriptum.test.ParentUiTest
import kotlin.random.Random

/**
 * Parent class for tests of [AlarmActivity] with different themes.
 */
abstract class ParentAlarmAnimTest(@Theme private val theme: Int) : ParentUiTest(), IColorTest {

    override fun startTest(@Color color: Int) {
        setupTheme(theme)

        with(preferenceRepo) {
            signal = Random.nextInt(from = 1, until = 3)
            volume = Random.nextInt(from = 50, until = 100)
            volumeIncrease = Random.nextBoolean()
        }

        val noteItem = data.clear().let {
            return@let if (Random.nextBoolean()) {
                it.insertText(it.textNote.apply { this.color = color })
            } else {
                it.insertRoll(it.rollNote.apply { this.color = color })
            }
        }

        launchAlarm(noteItem) { waitAfter(TEST_TIME) { openAlarm(noteItem) } }
    }

    companion object {
        private const val TEST_TIME = 6000L
    }
}