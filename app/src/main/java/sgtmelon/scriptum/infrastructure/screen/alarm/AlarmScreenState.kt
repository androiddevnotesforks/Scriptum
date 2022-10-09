package sgtmelon.scriptum.infrastructure.screen.alarm

import java.util.Calendar
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.key.Repeat

/**
 * States (work stages) of [AlarmActivity] screen.
 */
sealed class AlarmScreenState {

    class Setup(val noteItem: NoteItem, val melodyUri: String?) : AlarmScreenState()

    class Postpone(
        val noteId: Long,
        val repeat: Repeat,
        val calendar: Calendar
    ) : AlarmScreenState()

    object Close : AlarmScreenState()
}