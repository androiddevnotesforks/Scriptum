package sgtmelon.scriptum.infrastructure.screen.alarm.state

import java.util.Calendar
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.key.preference.Repeat
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmActivity

/**
 * States (work stages) of [AlarmActivity] screen.
 */
sealed class ScreenState {

    class Setup(val noteItem: NoteItem, val melodyUri: String?) : ScreenState()

    class Postpone(
        val noteId: Long,
        val repeat: Repeat,
        val calendar: Calendar
    ) : ScreenState()

    object Close : ScreenState()
}