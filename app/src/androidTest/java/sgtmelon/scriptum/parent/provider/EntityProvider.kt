package sgtmelon.scriptum.parent.provider

import kotlin.random.Random
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.cleanup.domain.model.key.NoteType
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.scriptum.parent.provider.DateProvider.nextDate
import sgtmelon.test.common.nextString

object EntityProvider {

    fun nextNoteEntity(
        id: Long,
        create: String = nextDate(),
        change: String = nextDate(),
        text: String = nextString(),
        name: String = nextString(),
        color: Color = Color.values().random(),
        type: NoteType = NoteType.values().random()
    ): NoteEntity {
        return NoteEntity(
            id = id, create = create, change = change,
            text = text, name = name, color = color, type = type
        )
    }

    fun nextAlarmEntity(id: Long, noteId: Long, date: String = nextDate()): AlarmEntity {
        return AlarmEntity(id, noteId, date)
    }

    fun nextRollVisibleEntity(
        id: Long,
        noteId: Long,
        value: Boolean = Random.nextBoolean()
    ): RollVisibleEntity {
        return RollVisibleEntity(id, noteId, value)
    }

    fun nextRollEntity(
        id: Long,
        noteId: Long,
        position: Int,
        isCheck: Boolean = Random.nextBoolean(),
        text: String = nextString()
    ): RollEntity {
        return RollEntity(id, noteId, position, isCheck, text)
    }
}