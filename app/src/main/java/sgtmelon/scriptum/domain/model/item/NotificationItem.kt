package sgtmelon.scriptum.domain.model.item

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.TypeConverters
import sgtmelon.scriptum.data.room.converter.type.NoteTypeConverter
import sgtmelon.scriptum.domain.model.data.DbData
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.presentation.adapter.NotificationAdapter

/**
 * Model for store short information about note and alarm, use in [NotificationAdapter].
 */
@TypeConverters(NoteTypeConverter::class)
data class NotificationItem(@Embedded val note: Note, @Embedded val alarm: Alarm) {

    data class Note(@ColumnInfo(name = DbData.Note.ID) val id: Long,
                    @ColumnInfo(name = DbData.Note.NAME) val name: String,
                    @ColumnInfo(name = DbData.Note.COLOR) val color: Int,
                    @ColumnInfo(name = DbData.Note.TYPE) val type: NoteType)

    data class Alarm(@ColumnInfo(name = DbData.Alarm.ID) val id: Long,
                     @ColumnInfo(name = DbData.Alarm.DATE) val date: String)

}