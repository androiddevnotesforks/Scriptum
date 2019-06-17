package sgtmelon.scriptum.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.TypeConverters
import sgtmelon.scriptum.model.data.DbData
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.converter.NoteTypeConverter

/**
 * Модель для хранения краткой информации о заметке (необходимой для отображения элемента списка)
 * и информацию об уведомлении к ней
 *
 * @author SerjantArbuz
 */
@TypeConverters(NoteTypeConverter::class)
data class NotificationItem(@Embedded val note: Note, @Embedded val alarm: Alarm) {

    data class Note(@ColumnInfo(name = DbData.Note.ID) val id: Long,
                    @ColumnInfo(name = DbData.Note.NAME) var name: String,
                    @ColumnInfo(name = DbData.Note.COLOR) val color: Int,
                    @ColumnInfo(name = DbData.Note.TYPE) val type: NoteType)

    data class Alarm(@ColumnInfo(name = DbData.Alarm.ID) val id: Long,
                     @ColumnInfo(name = DbData.Alarm.DATE) val date: String)

}