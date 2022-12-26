package sgtmelon.scriptum.cleanup.domain.model.item

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.TypeConverters
import sgtmelon.scriptum.infrastructure.adapter.NotificationAdapter
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.converter.key.NoteTypeConverter
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.database.DbData.Alarm as AlarmDb
import sgtmelon.scriptum.infrastructure.database.DbData.Note as NoteDb

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
}