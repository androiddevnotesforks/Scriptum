package sgtmelon.scriptum.source.provider

import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.source.provider.DateProvider.nextDate
import sgtmelon.test.common.nextString
import sgtmelon.test.common.oneFourthChance
import kotlin.math.abs
import kotlin.random.Random

object EntityProvider {

    fun nextNoteEntity(
        id: Long,
        create: String = nextDate(),
        change: String = nextDate(),
        text: String = nextString(),
        name: String = nextString(),
        color: Color = Color.values().random(),
        type: NoteType = NoteType.values().random(),
        rankId: Long = if (oneFourthChance()) abs(Random.nextLong()) else -1,
        rankPs: Int = rankId.toInt(),
        isBin: Boolean = Random.nextBoolean(),
        isStatus: Boolean = Random.nextBoolean()
    ): NoteEntity {
        return NoteEntity(
            id = id, create = create, change = change,
            text = text, name = name, color = color, type = type,
            rankId = rankId, rankPs = rankPs, isBin = isBin, isStatus = isStatus
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