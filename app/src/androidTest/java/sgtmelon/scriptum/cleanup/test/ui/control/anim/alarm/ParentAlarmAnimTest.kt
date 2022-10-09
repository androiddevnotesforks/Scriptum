package sgtmelon.scriptum.cleanup.test.ui.control.anim.alarm

import kotlin.random.Random
import sgtmelon.scriptum.cleanup.basic.extension.waitAfter
import sgtmelon.scriptum.cleanup.test.parent.situation.IColorTest
import sgtmelon.scriptum.infrastructure.converter.SignalConverter
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmActivity
import sgtmelon.scriptum.parent.ParentUiTest

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
            volumePercent = Random.nextInt(from = 50, until = 100)
            isVolumeIncrease = Random.nextBoolean()
        }

        val noteItem = db.clear().let {
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