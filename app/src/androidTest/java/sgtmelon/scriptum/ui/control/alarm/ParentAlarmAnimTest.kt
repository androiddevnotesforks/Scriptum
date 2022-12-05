package sgtmelon.scriptum.ui.control.alarm

import kotlin.random.Random
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmActivity
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.ui.cases.value.ColorCase
import sgtmelon.test.cappuccino.utils.await

/**
 * Parent class for tests of [AlarmActivity] with different themes.
 */
abstract class ParentAlarmAnimTest(
    private val theme: ThemeDisplayed
) : ParentUiTest(),
    ColorCase {

    override fun startTest(value: Color) {
        setupTheme(theme)

        val noteItem = if (Random.nextBoolean()) {
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