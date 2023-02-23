package sgtmelon.scriptum.cleanup.domain.model.item

import sgtmelon.scriptum.infrastructure.database.DbData.Alarm

// TODO may be convert date into Calendar?
data class NoteAlarm(var id: Long = Alarm.Default.ID, var date: String = Alarm.Default.DATE)