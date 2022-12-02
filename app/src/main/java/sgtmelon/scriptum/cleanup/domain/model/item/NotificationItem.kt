package sgtmelon.scriptum.cleanup.domain.model.item

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.TypeConverters
import org.json.JSONObject
import sgtmelon.scriptum.infrastructure.adapter.NotificationAdapter
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.converter.key.NoteTypeConverter
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Alarm as AlarmDb
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Note as NoteDb

/**
 * Model for store short information about note and alarm, use in [NotificationAdapter].
 */
@TypeConverters(NoteTypeConverter::class, ColorConverter::class)
data class NotificationItem(@Embedded val note: Note, @Embedded val alarm: Alarm) {

    data class Note(
        @ColumnInfo(name = NoteDb.ID) val id: Long,
        @ColumnInfo(name = NoteDb.NAME) val name: String,
        @ColumnInfo(name = NoteDb.COLOR) val color: Color,
        @ColumnInfo(name = NoteDb.TYPE) val type: NoteType
    )

    data class Alarm(
        @ColumnInfo(name = AlarmDb.ID) val id: Long,
        @ColumnInfo(name = AlarmDb.DATE) val date: String
    )

    // TODO add converter toJson and back
    fun toJson(): String = JSONObject().apply {
        put(NoteDb.ID, note.id)
        put(NoteDb.NAME, note.name)
        put(NoteDb.COLOR, note.color.ordinal)
        put(NoteDb.TYPE, note.type.ordinal)
        put(AlarmDb.ID, alarm.id)
        put(AlarmDb.DATE, alarm.date)
    }.toString()

    companion object {

        // TODO add converter toJson and back
        operator fun get(data: String): NotificationItem? = try {
            val typeConverter = NoteTypeConverter()
            val colorConverter = ColorConverter()

            JSONObject(data).let {
                val type = typeConverter.toEnum(it.getInt(NoteDb.TYPE)) ?: return@let null
                val color = colorConverter.toEnum(it.getInt(NoteDb.COLOR)) ?: return@let null

                val note = Note(it.getLong(NoteDb.ID), it.getString(NoteDb.NAME), color, type)
                val alarm = Alarm(it.getLong(AlarmDb.ID), it.getString(AlarmDb.DATE))

                return@let NotificationItem(note, alarm)
            }
        } catch (e: Throwable) {
            null
        }
    }
}