package sgtmelon.scriptum.cleanup.domain.model.item

import sgtmelon.scriptum.infrastructure.database.DbData.Alarm

data class NoteAlarm(var id: Long = Alarm.Default.ID, var date: String = Alarm.Default.DATE)