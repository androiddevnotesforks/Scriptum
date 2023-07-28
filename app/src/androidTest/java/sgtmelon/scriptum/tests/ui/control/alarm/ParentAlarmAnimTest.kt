package sgtmelon.scriptum.tests.ui.control.alarm

import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmActivity
import sgtmelon.scriptum.source.cases.value.ColorCase
import sgtmelon.scriptum.source.ui.tests.ParentUiControlTest
import sgtmelon.test.cappuccino.utils.await
import sgtmelon.test.common.halfChance

/**
 * Parent class for tests of [AlarmActivity] with different themes.
 */
abstract class ParentAlarmAnimTest(
    private val theme: ThemeDisplayed
) : ParentUiControlTest(),
    ColorCase {

    override fun startTest(value: Color) {
        setupTheme(theme)

        val noteItem = if (halfChance()) {
            db.insertText(db.textNote.apply { this.color = value })
        } else {
            db.insertRoll(db.rollNote.apply { this.color = value })
        }

        launchSplashAlarm(noteItem) { alarmScreen(noteItem) { await(TEST_TIME) } }
    }

    companion object {
        private const val TEST_TIME = 6000L
    }
}