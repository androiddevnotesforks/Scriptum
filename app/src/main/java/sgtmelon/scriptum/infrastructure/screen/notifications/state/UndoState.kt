package sgtmelon.scriptum.infrastructure.screen.notifications.state

import java.util.Calendar

sealed class UndoState {

    class NotifyInfoCount(val count: Int) : UndoState()

    class NotifyAlarm(val id: Long, val calendar: Calendar) : UndoState()
}