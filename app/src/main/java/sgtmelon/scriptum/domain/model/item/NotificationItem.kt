package sgtmelon.scriptum.domain.model.item

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.TypeConverters
import org.json.JSONObject
import sgtmelon.scriptum.data.room.converter.type.NoteTypeConverter
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.presentation.adapter.NotificationAdapter
import sgtmelon.scriptum.domain.model.data.DbData.Alarm as AlarmDb
import sgtmelon.scriptum.domain.model.data.DbData.Note as NoteDb

/**
 * Model for store short information about note and alarm, use in [NotificationAdapter].
 */
@TypeConverters(NoteTypeConverter::class)
data class NotificationItem(@Embedded val note: Note, @Embedded val alarm: Alarm) {

    data class Note(
        @ColumnInfo(name = NoteDb.ID) val id: Long,
        @ColumnInfo(name = NoteDb.NAME) val name: String,
        @ColumnInfo(name = NoteDb.COLOR) val color: Int,
        @ColumnInfo(name = NoteDb.TYPE) val type: NoteType
    )

    data class Alarm(
        @ColumnInfo(name = AlarmDb.ID) val id: Long,
        @ColumnInfo(name = AlarmDb.DATE) val date: String
    )

    fun toJson(): String = JSONObject().apply {
        put(NoteDb.ID, note.id)
        put(NoteDb.NAME, note.name)
        put(NoteDb.COLOR, note.color)
        put(NoteDb.TYPE, note.type.ordinal)
        put(AlarmDb.ID, alarm.id)
        put(AlarmDb.DATE, alarm.date)
    }.toString()

    companion object {
        operator fun get(data: String): NotificationItem? = try {
            JSONObject(data).let {
                val type = NoteType.values().getOrNull(it.getInt(NoteDb.TYPE)) ?: return@let null
                val note = Note(
                    it.getLong(NoteDb.ID), it.getString(NoteDb.NAME),
                    it.getInt(NoteDb.COLOR), type
                )
                val alarm = Alarm(it.getLong(AlarmDb.ID), it.getString(AlarmDb.DATE))

                return@let NotificationItem(note, alarm)
            }
        } catch (e: Throwable) {
            null
        }
    }
}