package sgtmelon.scriptum.cleanup.test.ui.control.anim.alarm

import kotlin.random.Random
import sgtmelon.scriptum.cleanup.basic.extension.waitAfter
import sgtmelon.scriptum.infrastructure.converter.SignalConverter
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmActivity
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.ui.cases.value.ColorCase

/**
 * Parent class for tests of [AlarmActivity] with different themes.
 */
abstract class ParentAlarmAnimTest(
    private val theme: ThemeDisplayed
) : ParentUiTest(),
    ColorCase {

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

        launchAlarm(noteItem) { waitAfter(TEST_TIME) { alarmScreen(noteItem) } }
    }

    companion object {
        private const val TEST_TIME = 6000L
    }
}