package sgtmelon.scriptum.infrastructure.screen.data

import java.util.Calendar
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.notification.AlarmActivity
import sgtmelon.scriptum.infrastructure.model.key.Repeat

/**
 * States (work stages) of [AlarmActivity] screen.
 */
sealed class AlarmScreenState {

    class Setup(val melodyUri: String?) : AlarmScreenState()

    class Postpone(
        val noteId: Long,
        val repeat: Repeat,
        val calendar: Calendar
    ) : AlarmScreenState()

    object Close : AlarmScreenState()
}