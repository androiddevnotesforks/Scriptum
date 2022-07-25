package sgtmelon.scriptum.test.ui.control.anim.alarm

import kotlin.random.Random
import sgtmelon.scriptum.basic.extension.waitAfter
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.notification.AlarmActivity
import sgtmelon.scriptum.infrastructure.converter.SignalConverter
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.test.parent.situation.IColorTest

/**
 * Parent class for tests of [AlarmActivity] with different themes.
 */
abstract class ParentAlarmAnimTest(
    private val theme: ThemeDisplayed
) : ParentUiTest(),
    IColorTest {

    override fun startTest(value: Color) {
        setupTheme(theme)

        with(preferences) {
            signal = SignalConverter().toString(BooleanArray(size = 2) { Random.nextBoolean() })
            volume = Random.nextInt(from = 50, until = 100)
            isVolumeIncrease = Random.nextBoolean()
        }

        val noteItem = data.clear().let {
            return@let if (Random.nextBoolean()) {
                it.insertText(it.textNote.apply { this.color = value.ordinal })
            } else {
                it.insertRoll(it.rollNote.apply { this.color = value.ordinal })
            }
        }

        launchAlarm(noteItem) { waitAfter(TEST_TIME) { openAlarm(noteItem) } }
    }

    companion object {
        private const val TEST_TIME = 6000L
    }
}