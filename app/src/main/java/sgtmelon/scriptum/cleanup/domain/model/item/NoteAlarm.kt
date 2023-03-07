package sgtmelon.scriptum.cleanup.domain.model.item

import kotlinx.serialization.Serializable
import sgtmelon.scriptum.infrastructure.database.DbData.Alarm

// TODO may be convert date into Calendar?
@Serializable
data class NoteAlarm(var id: Long = Alarm.Default.ID, var date: String = Alarm.Default.DATE)