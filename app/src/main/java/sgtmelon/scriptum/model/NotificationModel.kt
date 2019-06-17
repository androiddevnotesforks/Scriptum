package sgtmelon.scriptum.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import sgtmelon.scriptum.model.data.DbData.Note
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.entity.AlarmEntity

data class NotificationModel(
        @ColumnInfo(name = Note.ID) val id: Long,
        @ColumnInfo(name = Note.COLOR) val color: Int,
        @ColumnInfo(name = Note.TYPE) val type: NoteType,
        @Embedded val alarmEntity: AlarmEntity
)